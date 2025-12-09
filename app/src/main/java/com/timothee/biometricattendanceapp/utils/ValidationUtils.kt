package com.timothee.biometricattendanceapp.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        if (password.length < Constants.MIN_PASSWORD_LENGTH) return false

        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }

        return hasUpperCase && hasLowerCase && hasDigit
    }

    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Password cannot be empty"
            password.length < Constants.MIN_PASSWORD_LENGTH ->
                "Password must be at least ${Constants.MIN_PASSWORD_LENGTH} characters"
            !password.any { it.isUpperCase() } ->
                "Password must contain at least one uppercase letter"
            !password.any { it.isLowerCase() } ->
                "Password must contain at least one lowercase letter"
            !password.any { it.isDigit() } ->
                "Password must contain at least one number"
            else -> null
        }
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
}