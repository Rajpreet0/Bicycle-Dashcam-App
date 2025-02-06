package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.NetworkHelper
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class SettingsEditActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val networkHelper = NetworkHelper()

    // UI Elements
    private lateinit var lastnameInput: EditText
    private lateinit var firstnameInput: EditText
    private lateinit var birthdayInput: EditText
    private lateinit var birthplaceInput: EditText
    private lateinit var birthcountryInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var telephoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_edit)

        sessionManager = SessionManager(this)

        val parentLayout = findViewById<LinearLayout>(R.id.registerLL)
        var idCounter = 2000 // Different ID range to avoid conflicts

        lastnameInput = createEditText("Nachname", sessionManager.getLastName(), idCounter++)
        parentLayout.addView(lastnameInput)

        firstnameInput = createEditText("Vorname", sessionManager.getFirstName(), idCounter++)
        parentLayout.addView(firstnameInput)

        val genderLabel = TextView(this)
        genderLabel.text = "Geschlecht"
        genderLabel.setPadding(20, 10, 20, 10)
        parentLayout.addView(genderLabel)

        genderGroup = createRadioGroup(sessionManager.getGender(), idCounter++)
        parentLayout.addView(genderGroup)

        birthdayInput = createEditText("Geburtsdatum", sessionManager.getBirthday(), idCounter++)
        parentLayout.addView(birthdayInput)

        birthplaceInput = createEditText("Geburtsort", sessionManager.getBirthplace(), idCounter++)
        parentLayout.addView(birthplaceInput)

        birthcountryInput = createEditText("Geburtsland", sessionManager.getBirthCountry(), idCounter++)
        parentLayout.addView(birthcountryInput)

        addressInput = createEditText("Anschrift", sessionManager.getAddress(), idCounter++)
        parentLayout.addView(addressInput)

        telephoneInput = createEditText("Telefonnummer", sessionManager.getTelephoneNumber(), idCounter++)
        parentLayout.addView(telephoneInput)

        emailInput = createEditText("Email", sessionManager.getUserEmail(), idCounter++)
        parentLayout.addView(emailInput)

        btnUpdate = Button(this).apply {
            text = "Update"
            setOnClickListener { updateBtn() }
        }
        parentLayout.addView(btnUpdate)
    }

    private fun createEditText(hint: String, text: String?, id: Int): EditText {
        return EditText(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.hint = hint
            this.id = id
            setPadding(20, 20, 20, 20)
            setText(text) // Pre-fill data
        }
    }

    private fun createRadioGroup(selectedGender: String?, id: Int): RadioGroup {
        return RadioGroup(this).apply {
            orientation = RadioGroup.HORIZONTAL
            this.id = id
            val options = listOf("männlich", "weiblich", "divers")
            options.forEach { option ->
                val radioButton = RadioButton(this@SettingsEditActivity)
                radioButton.text = option
                if (option.equals(selectedGender, ignoreCase = true)) {
                    radioButton.isChecked = true
                }
                addView(radioButton)
            }
        }
    }

    fun updateBtn() {
        val selectedGenderId = genderGroup.checkedRadioButtonId
        val selectedGender = findViewById<RadioButton>(selectedGenderId)?.text?.toString() ?: ""

        val userId = sessionManager.getUserId() ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = networkHelper.updateUserData(
                    id = userId,
                    lastname = lastnameInput.text.toString(),
                    firstname = firstnameInput.text.toString(),
                    gender = selectedGender,
                    birthday = birthdayInput.text.toString(),
                    birthplace = birthplaceInput.text.toString(),
                    birthcountry = birthcountryInput.text.toString(),
                    address = addressInput.text.toString(),
                    telephoneNumber = telephoneInput.text.toString(),
                    email = emailInput.text.toString()
                )

                val userData = JSONObject(response.toString())

                withContext(Dispatchers.Main) {
                    Log.d("Data from Update:", userData.toString())
                    Toast.makeText(
                        applicationContext,
                        "User updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SettingsEditActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Log.d("SERVER ERROR", "Update failed - ${e.message}")
                    Toast.makeText(applicationContext, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
    fun historyButton(view: View) {
        val intent: Intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    fun settingsButton(view: View) {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun dashboardButton(view: View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun backButton(view: View) {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
