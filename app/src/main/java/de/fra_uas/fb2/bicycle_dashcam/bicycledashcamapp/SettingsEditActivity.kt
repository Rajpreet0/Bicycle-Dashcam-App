package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Reference to your parent LinearLayout from XML
        val parentLayout = findViewById<LinearLayout>(R.id.registerLL)

        // Function to create an EditText dynamically
        fun createEditText(hint: String): EditText {
            val editText = EditText(this)
            editText.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            editText.hint = hint
            editText.setPadding(20, 20, 20, 20)
            return editText
        }

        // Function to create a RadioGroup dynamically
        fun createRadioGroup(): RadioGroup {
            val radioGroup = RadioGroup(this)
            radioGroup.orientation = RadioGroup.HORIZONTAL
            val options = listOf("männlich", "weiblich", "divers")
            for (option in options) {
                val radioButton = RadioButton(this)
                radioButton.text = option
                radioGroup.addView(radioButton)
            }
            return radioGroup
        }

        // Add form fields directly to the parent layout
        parentLayout.addView(createEditText("Nachname"))
        parentLayout.addView(createEditText("Vorname"))

        // Add Gender Selection (RadioGroup)
        val genderLabel = TextView(this)
        genderLabel.text = "Geschlecht"
        genderLabel.setPadding(20, 10, 20, 10)
        parentLayout.addView(genderLabel)
        parentLayout.addView(createRadioGroup())

        parentLayout.addView(createEditText("Geburtsdatum"))
        parentLayout.addView(createEditText("Geburtsort"))
        parentLayout.addView(createEditText("Geburtsland"))
        parentLayout.addView(createEditText("Anschrift"))
        parentLayout.addView(createEditText("Telefonnummer"))
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