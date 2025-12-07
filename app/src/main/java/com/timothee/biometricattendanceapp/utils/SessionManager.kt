package com.timothee.biometricattendance.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun saveLoginSession(userId: Int, email: String) {
        prefs.edit().apply {
            putInt(Constants.KEY_USER_ID, userId)
            putString(Constants.KEY_USER_EMAIL, email)
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): Int {
        return prefs.getInt(Constants.KEY_USER_ID, -1)
    }

    fun getUserEmail(): String? {
        return prefs.getString(Constants.KEY_USER_EMAIL, null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}