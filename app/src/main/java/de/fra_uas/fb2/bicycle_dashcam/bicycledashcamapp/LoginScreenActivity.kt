package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun loginButton(view: View){
        val intent: Intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun backButton(view: View){
        val intent: Intent = Intent(this, StartScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}