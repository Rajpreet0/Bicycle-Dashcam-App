package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.widget.Toast

class WifiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: ConnectionActivity
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                if (activity.hasPermissions()) {
                    try {
                        manager.requestPeers(channel) { peers ->
                            activity.updateDeviceList(peers)
                        }
                    } catch (e: SecurityException) {
                        Toast.makeText(context, "Berechtigungsfehler: ${e.message}", Toast.LENGTH_SHORT).show()
                        activity.checkAndRequestPermissions()
                    }
                } else {
                    Toast.makeText(context, "Berechtigungen erforderlich, um nach Peers zu suchen.", Toast.LENGTH_SHORT).show()
                    activity.checkAndRequestPermissions()
                }
            }
        }
    }
}