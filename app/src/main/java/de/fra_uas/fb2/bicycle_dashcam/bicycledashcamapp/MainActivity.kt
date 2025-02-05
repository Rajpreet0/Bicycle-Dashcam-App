package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.JsonObject
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.NetworkHelper
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val networkHelper = NetworkHelper();
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun historyButton(view: View){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun settingsButton(view: View){
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun dashboardButton(view: View){
        val intent: Intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}