package com.timothee.biometricattendance.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Centralized navigation routes for the app.
 *
 * Each screen has a unique route string and
 * optional helper functions for generating safe routes
 * and extracting arguments.
 */
sealed class Screen(val route: String) {

    // ---------------- AUTH SCREENS ---------------- //

    object SignUp : Screen("signup")

    object SignIn : Screen("signin")

    /**
     * Set password screen requires parameters:
     *  - email
     *  - name
     *
     * Both values are URL-encoded to prevent route-breaking characters.
     */
    object SetPassword : Screen("set_password/{email}/{name}") {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun createRoute(email: String, name: String): String {
            val encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8)
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8)
            return "set_password/$encodedEmail/$encodedName"
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun decodeEmail(encoded: String): String =
            URLDecoder.decode(encoded, StandardCharsets.UTF_8)

        fun decodeName(encoded: String): String =
            URLDecoder.decode(encoded, StandardCharsets.UTF_8)
    }

    // ---------------- HOME & ATTENDANCE ---------------- //

    object Home : Screen("home")

    object AttendanceHistory : Screen("attendance_history")
}
