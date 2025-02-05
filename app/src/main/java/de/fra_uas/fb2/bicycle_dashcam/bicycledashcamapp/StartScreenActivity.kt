package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.SessionManager

class StartScreenActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)
        if(sessionManager.isLoggedIn()){
            navigateToDashboard()
        }

    }
    fun loginButton(view: View){
        val intent: Intent = Intent(this, LoginScreenActivity::class.java)
        startActivity(intent)
    }
    fun registerOnClick(view: View){
        val intent: Intent = Intent(this, RegisterScreenActivity::class.java)
        startActivity(intent)
    }

    fun navigateToDashboard() {
        val intent: Intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}