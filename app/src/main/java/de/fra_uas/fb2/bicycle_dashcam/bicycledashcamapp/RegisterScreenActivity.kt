package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.NetworkHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class RegisterScreenActivity : AppCompatActivity() {

    private lateinit var lastnameInput: EditText
    private lateinit var firstnameInput: EditText
    private lateinit var birthdayInput: EditText
    private lateinit var birthplaceInput: EditText
    private lateinit var birthcountryInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var telephoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var repeatPasswordInput: EditText
    private lateinit var genderGroup: RadioGroup

    private  val networkHelper = NetworkHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val parentLayout = findViewById<LinearLayout>(R.id.registerLL)
        var idCounter = 1000

        lastnameInput = createEditText("Nachname", idCounter++)
        parentLayout.addView(lastnameInput)

        firstnameInput = createEditText("Vorname", idCounter++)
        parentLayout.addView(firstnameInput)

        val genderLabel = TextView(this)
        genderLabel.text = "Geschlecht"
        genderLabel.setPadding(20, 10, 20, 10)
        parentLayout.addView(genderLabel)

        genderGroup = createRadioGroup(idCounter++)
        parentLayout.addView(genderGroup)

        birthdayInput = createEditText("Geburtsdatum", idCounter++)
        parentLayout.addView(birthdayInput)

        birthplaceInput = createEditText("Geburtsort", idCounter++)
        parentLayout.addView(birthplaceInput)

        birthcountryInput = createEditText("Geburtsland", idCounter++)
        parentLayout.addView(birthcountryInput)

        addressInput = createEditText("Anschrift", idCounter++)
        parentLayout.addView(addressInput)

        telephoneInput = createEditText("Telefonnummer", idCounter++)
        parentLayout.addView(telephoneInput)

        emailInput = createEditText("Email", idCounter++)
        parentLayout.addView(emailInput)

        passwordInput = createEditText("Password", idCounter++)
        parentLayout.addView(passwordInput)

        repeatPasswordInput = createEditText("Repeat Password", idCounter++)
        parentLayout.addView(repeatPasswordInput)

    }

    private fun createEditText(hint: String, id: Int): EditText {
        return EditText(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.hint = hint
            this.id = id
            setPadding(20, 20, 20, 20)
        }
    }

    private fun createRadioGroup(id: Int): RadioGroup {
        return RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
            this.id = id
            val options = listOf("männlich", "weiblich", "divers")
            options.forEach { option ->
                val radioButton = RadioButton(this@RegisterScreenActivity)
                radioButton.text = option
                addView(radioButton)
            }
        }
    }

    fun registerButton(view: View){
        val selectedGenderId = genderGroup.checkedRadioButtonId
        val selectedGender = findViewById<RadioButton>(selectedGenderId)
        val gender = selectedGender?.text?.toString() ?: ""

        val intent: Intent = Intent(this, DashboardActivity::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkHelper.register(
                        lastnameInput.text.toString(),
                        firstnameInput.text.toString(),
                        gender,
                        birthdayInput.text.toString(),
                        birthplaceInput.text.toString(),
                        birthcountryInput.text.toString(),
                        addressInput.text.toString(),
                        telephoneInput.text.toString(),
                        emailInput.text.toString(),
                        passwordInput.text.toString())

                val userData = JSONObject(response.toString())
                withContext(Dispatchers.Main) {
                    Log.d("Data from Login:", userData.toString())
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
        val intent: Intent = Intent(this,StartScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}