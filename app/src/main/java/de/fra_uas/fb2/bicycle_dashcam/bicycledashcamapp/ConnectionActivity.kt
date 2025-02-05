package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.Locale

class ConnectionActivity : AppCompatActivity() {

    private lateinit var wifiP2pManager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var receiver: BroadcastReceiver
    private lateinit var deviceListView: ListView
    private lateinit var deviceAdapter: ArrayAdapter<String>
    private lateinit var connectionStatus: TextView
    private lateinit var disconnectButton: Button
    private lateinit var locationManager: LocationManager
    private lateinit var locationText: TextView

    private val deviceNames = mutableListOf<String>()
    private val deviceList = mutableListOf<WifiP2pDevice>()
    private val LOCATION_PERMISSION_REQUEST_CODE = 2

    private var serverJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

        wifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = wifiP2pManager.initialize(this, mainLooper, null)

        connectionStatus = findViewById(R.id.connection_status)
        deviceListView = findViewById(R.id.device_list)
        disconnectButton = findViewById(R.id.disconnect_button)
        locationText = findViewById(R.id.location_text)

        deviceAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames)
        deviceListView.adapter = deviceAdapter

        val searchButton: Button = findViewById(R.id.search_button)
        searchButton.setOnClickListener { discoverPeers() }

        disconnectButton.setOnClickListener { disconnect() }

        deviceListView.setOnItemClickListener { _, _, position, _ ->
            val device = deviceList[position]
            connectToDevice(device)
        }

        val receiveButton: Button = findViewById(R.id.receive_button)
        receiveButton.setOnClickListener { startServerSocket() }

        val getLocationButton: Button = findViewById(R.id.get_location_button)
        getLocationButton.setOnClickListener { requestLocation() }

        checkAndRequestPermissions()
    }

    fun requestLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            updateLocationUI(location)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            updateLocationUI(location)
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun updateLocationUI(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val street = address.thoroughfare ?: "Unbekannte Straße"
                val houseNumber = address.subThoroughfare ?: ""
                val postalCode = address.postalCode ?: ""
                val city = address.locality ?: "Unbekannte Stadt"

                runOnUiThread {
                    locationText.text = "Standort: \n$street $houseNumber\n$postalCode $city"
                }
            } else {
                runOnUiThread {
                    locationText.text = "Standort konnte nicht ermittelt werden"
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUiThread {
                locationText.text = "Fehler beim Abrufen des Standortes"
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation()
            } else {
                Toast.makeText(this, "Standortberechtigung verweigert", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun discoverPeers() {
        if (hasPermissions()) {
            try {
                wifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        Toast.makeText(this@ConnectionActivity, "Suche gestartet...", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(reason: Int) {
                        Toast.makeText(this@ConnectionActivity, "Peer-Suche fehlgeschlagen: $reason", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: SecurityException) {
                Toast.makeText(this, "Berechtigungsfehler: ${e.message}", Toast.LENGTH_LONG).show()
                checkAndRequestPermissions()
            }
        } else {
            Toast.makeText(this, "Berechtigungen erforderlich, um nach Geräten zu suchen.", Toast.LENGTH_SHORT).show()
            checkAndRequestPermissions()
        }
    }

    fun connectToDevice(device: WifiP2pDevice) {
        if (hasPermissions()) {
            val config = WifiP2pConfig().apply {
                deviceAddress = device.deviceAddress
            }

            try {
                wifiP2pManager.connect(channel, config, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        connectionStatus.text = "Status: Verbunden mit ${device.deviceName}"
                        disconnectButton.visibility = Button.VISIBLE
                    }

                    override fun onFailure(reason: Int) {
                        connectionStatus.text = "Status: Verbindung fehlgeschlagen ($reason)"
                    }
                })
            } catch (e: SecurityException) {
                Toast.makeText(this, "Sicherheitsausnahme: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Berechtigungen erforderlich, um eine Verbindung herzustellen.", Toast.LENGTH_SHORT).show()
            checkAndRequestPermissions()
        }
    }

    fun disconnect() {
        wifiP2pManager.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                connectionStatus.text = "Status: Nicht verbunden"
                disconnectButton.visibility = Button.GONE
            }

            override fun onFailure(reason: Int) {
                connectionStatus.text = "Status: Trennung fehlgeschlagen ($reason)"
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
        receiver = WifiDirectBroadcastReceiver(wifiP2pManager, channel, this)
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    fun hasPermissions(): Boolean {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun checkAndRequestPermissions() {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        val notGrantedPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), 1)
        }
    }

    fun updateDeviceList(peers: WifiP2pDeviceList) {
        deviceNames.clear()
        deviceList.clear()

        peers.deviceList.forEach { device ->
            deviceNames.add(device.deviceName ?: "Unbekanntes Gerät")
            deviceList.add(device)
        }

        deviceAdapter.notifyDataSetChanged()
    }

    fun startServerSocket() {
        serverJob?.cancel()

        serverJob = CoroutineScope(Dispatchers.IO).launch {
            val port = 8988

            var serverSocket: ServerSocket? = null
            var clientSocket: Socket? = null

            try {
                serverSocket = ServerSocket(port)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConnectionActivity, "Warte auf Datei vom Pi...", Toast.LENGTH_SHORT).show()
                }

                clientSocket = serverSocket.accept()

                val inputStream = clientSocket.getInputStream()

                val file = File(getExternalFilesDir(null), "received_image.jpg")

                file.outputStream().use { fileOut ->
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (true) {
                        bytesRead = inputStream.read(buffer)
                        if (bytesRead == -1) break
                        fileOut.write(buffer, 0, bytesRead)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ConnectionActivity,
                        "Bild empfangen und gespeichert unter:\n${file.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConnectionActivity, "Fehler beim Empfangen: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } finally {
                clientSocket?.close()
                serverSocket?.close()
            }
        }
    }
    fun historyButton(view: View){
        val intent: Intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
    fun settingsButton(view: View){
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
    fun dashboardButton(view: View){
        val intent: Intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun generateButton(view: View) {
        val intent = Intent(this, ReportActivity::class.java)
        startActivity(intent)

    }
}
