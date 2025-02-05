package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun connectButton(view: View){

    }
    fun historyButton(view: View){
        val intent: Intent = Intent(this, HistoryActivity::class.java)
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