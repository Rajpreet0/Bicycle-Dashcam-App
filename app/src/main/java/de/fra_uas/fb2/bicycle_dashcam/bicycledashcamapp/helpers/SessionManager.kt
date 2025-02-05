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
        //TODO: Need here more
    }

    fun createLoginSession(userData: JSONObject) {
        val editor = prefs.edit()
        val userObject = userData.getJSONObject("user")

        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, userObject.getString("id"))
        editor.putString(KEY_USER_EMAIL, userObject.getString("email"))
        editor.putString(KEY_USER_FIRSTNAME, userObject.getString("firstname"))
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

}