package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.NetworkHelper
import de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class ReportActivity : AppCompatActivity() {

    private lateinit var lastnameInput: EditText
    private lateinit var firstnameInput: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var birthdayInput: EditText
    private lateinit var birthplaceInput: EditText
    private lateinit var birthcountryInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var telephoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var timeInput: EditText
    private lateinit var crimeStateInput: EditText
    private lateinit var crimeCountryInput: EditText
    private lateinit var crimeLocationInput: EditText
    private lateinit var crimeStreetInput: EditText
    private lateinit var btnUpdate: Button
    private lateinit var sessionManager: SessionManager
    private var networkHelper = NetworkHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        sessionManager = SessionManager(this)

        val parentLayout = findViewById<LinearLayout>(R.id.linearLayoutScrollView)
        var idCounter = 2000 // Unique ID range to avoid conflicts

        // Existing fields
        lastnameInput = createEditText("Nachname", sessionManager.getLastName(), idCounter++)
        parentLayout.addView(lastnameInput)

        firstnameInput = createEditText("Vorname", sessionManager.getFirstName(), idCounter++)
        parentLayout.addView(firstnameInput)

        val genderLabel = TextView(this).apply {
            text = "Geschlecht"
            setPadding(20, 10, 20, 10)
        }
        parentLayout.addView(genderLabel)

        genderGroup = createRadioGroup(sessionManager.getGender(), idCounter++)
        parentLayout.addView(genderGroup)

        birthdayInput = createEditText("Geburtsdatum", sessionManager.getBirthday(), idCounter++)
        parentLayout.addView(birthdayInput)

        birthplaceInput = createEditText("Geburtsort", sessionManager.getBirthplace(), idCounter++)
        parentLayout.addView(birthplaceInput)

        birthcountryInput =
            createEditText("Geburtsland", sessionManager.getBirthCountry(), idCounter++)
        parentLayout.addView(birthcountryInput)

        addressInput = createEditText("Anschrift", sessionManager.getAddress(), idCounter++)
        parentLayout.addView(addressInput)

        telephoneInput =
            createEditText("Telefonnummer", sessionManager.getTelephoneNumber(), idCounter++)
        parentLayout.addView(telephoneInput)

        emailInput = createEditText("Email", sessionManager.getUserEmail(), idCounter++)
        parentLayout.addView(emailInput)

        // New fields
//        dateInput = createEditText("Datum", sessionManager.getDate(), idCounter++)
//        parentLayout.addView(dateInput)
//
//        timeInput = createEditText("Uhrzeit", sessionManager.getTime(), idCounter++)
//        parentLayout.addView(timeInput)
//
//        crimeStateInput = createEditText("Tatbundesland", sessionManager.getCrimeState(), idCounter++)
//        parentLayout.addView(crimeStateInput)
//
//        crimeCountryInput = createEditText("Tatland", sessionManager.getCrimeCountry(), idCounter++)
//        parentLayout.addView(crimeCountryInput)
//
//        crimeLocationInput = createEditText("Tatort", sessionManager.getCrimeLocation(), idCounter++)
//        parentLayout.addView(crimeLocationInput)
//
//        crimeStreetInput = createEditText("Tatstraße", sessionManager.getCrimeStreet(), idCounter++)
//        parentLayout.addView(crimeStreetInput)
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
                val radioButton = RadioButton(this@ReportActivity).apply {
                    text = option
                    isChecked = option.equals(selectedGender, ignoreCase = true)
                }
                addView(radioButton)
            }
        }
    }

    fun generateButton(view: View) {

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
}
