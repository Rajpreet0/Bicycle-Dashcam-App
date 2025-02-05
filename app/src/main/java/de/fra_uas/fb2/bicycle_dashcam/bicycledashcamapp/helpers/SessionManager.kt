package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    companion object {
        private const val PREFS_NAME = "bicycle_dashcam_app_prefs_userData"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_SESSION_EXPIRY = "session_expiry"
        private const val SESSION_DURATION = 24*60*60*1000
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_FIRSTNAME = "first_name"
        private const val KEY_USER_LASTNAME = "last_name"
        private const val KEY_GENDER = "gender"
        private const val KEY_BIRTHDAY = "birthday"
        private const val KEY_BIRTHPLACE = "birthplace"
        private const val KEY_BIRTHCOUNTRY = "birthcountry"
        private const val KEY_ADDRESS = "address"
        private const val KEY_TELEPHONENUMBER = "telephone_number"
        private const val KEY_EMAIL = "email"
    }

    fun createLoginSession(userData: JSONObject) {
        val editor = prefs.edit()
        val userObject = userData.getJSONObject("user")

        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, userObject.getString("id"))
        editor.putString(KEY_USER_EMAIL, userObject.getString("email"))
        editor.putString(KEY_USER_FIRSTNAME, userObject.getString("firstname"))
        editor.putString(KEY_USER_LASTNAME, userObject.getString("lastname"))
        editor.putString(KEY_GENDER, userObject.getString("gender"))
        editor.putString(KEY_BIRTHDAY, userObject.getString("birthday"))
        editor.putString(KEY_BIRTHPLACE, userObject.getString("birthplace"))
        editor.putString(KEY_BIRTHCOUNTRY, userObject.getString("birthcountry"))
        editor.putString(KEY_ADDRESS, userObject.getString("address"))
        editor.putString(KEY_TELEPHONENUMBER, userObject.getString("telephone_number"))
        editor.putString(KEY_EMAIL, userObject.getString("email"))
        editor.putLong(KEY_SESSION_EXPIRY, System.currentTimeMillis() + SESSION_DURATION)
        editor.apply()
    }


    fun isLoggedIn(): Boolean {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val sessionExpriy = prefs.getLong(KEY_SESSION_EXPIRY, 0)
        if (isLoggedIn && System.currentTimeMillis() < sessionExpriy) {
            return true
        }
        logout()
        return false
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    // Getter and Setter Methods
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun getFirstName(): String? {
        return prefs.getString(KEY_USER_FIRSTNAME, null)
    }

    fun getLastName(): String? {
        return prefs.getString(KEY_USER_LASTNAME, null)
    }

    fun getGender(): String? {
        return prefs.getString(KEY_GENDER, null)
    }

    fun getBirthday(): String? {
        return prefs.getString(KEY_BIRTHDAY, null)
    }

    fun getBirthplace(): String? {
        return prefs.getString(KEY_BIRTHPLACE, null)
    }

    fun getBirthCountry(): String? {
        return prefs.getString(KEY_BIRTHCOUNTRY, null)
    }

    fun getAddress(): String? {
        return prefs.getString(KEY_ADDRESS, null)
    }

    fun getTelephoneNumber(): String? {
        return prefs.getString(KEY_TELEPHONENUMBER, null)
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }


}