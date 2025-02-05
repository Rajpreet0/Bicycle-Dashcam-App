package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.NetworkHelper
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class SettingsActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private  val networkHelper = NetworkHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)
    }

    fun deleteButton(view: View){
        val intent: Intent = Intent(this@SettingsActivity, StartScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkHelper.deleteAccount(sessionManager.getUserId().toString())
                val message = JSONObject(response.toString())
                withContext(Dispatchers.Main) {
                    Log.d("Message: ", message.toString())
                    sessionManager.logout()
                    startActivity(intent)
                }
            } catch (e: IOException)  {
                withContext(Dispatchers.Main) {
                    Log.d("SERVER ERROR", "Deleting Account Failed - ${e}")
                    Toast.makeText(applicationContext, "Deleting Account Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun logoutButton(view: View){
        val intent: Intent = Intent(this@SettingsActivity, StartScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        sessionManager.logout()
        startActivity(intent)
        finish()
    }

    fun nextButton(view: View){
        val intent: Intent = Intent(this, SettingsEditActivity::class.java)
        startActivity(intent)
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
}