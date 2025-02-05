package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var email: EditText;
    private lateinit var password: EditText;

    private  val networkHelper = NetworkHelper()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.editTextTextEmailAddress)
        password = findViewById(R.id.editTextTextPassword)


        sessionManager = SessionManager(this)

        if(sessionManager.isLoggedIn()) {
           navigateToDashboard()
        }
    }

    fun loginButton(view: View){

        val intent: Intent = Intent(this, DashboardActivity::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkHelper.login(email.text.toString(), password.text.toString())
                val userData = JSONObject(response.toString())
                withContext(Dispatchers.Main) {
                    Log.d("Data from Login:", userData.toString())
                    sessionManager.createLoginSession(userData)
                    startActivity(intent)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Log.d("SERVER ERROR", "Login failed - ${e}")
                    Toast.makeText(applicationContext, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun backButton(view: View){
        val intent: Intent = Intent(this, StartScreenActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun navigateToDashboard() {
        val intent: Intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}