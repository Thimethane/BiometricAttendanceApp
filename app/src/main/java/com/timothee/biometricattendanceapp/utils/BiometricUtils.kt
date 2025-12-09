package com.timothee.biometricattendanceapp.utils

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL

object BiometricUtils {

    /**
     * Check if device supports biometric authentication
     */
    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(getAuthenticators())) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> false
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> false
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> false
            else -> false
        }
    }

    /**
     * Check if biometric is enrolled
     */
    fun isBiometricEnrolled(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(getAuthenticators()) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Get biometric status message
     */
    fun getBiometricStatusMessage(context: Context): String {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(getAuthenticators())) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                "Biometric authentication is available"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                "No biometric hardware available on this device"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                "Biometric hardware is currently unavailable"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                "No biometric credentials enrolled. Please enroll in device settings"
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED ->
                "Security update required for biometric authentication"
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED ->
                "Biometric authentication not supported"
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN ->
                "Biometric status unknown"
            else -> "Unable to determine biometric availability"
        }
    }

    /**
     * Get appropriate authenticators based on Android version
     */
    private fun getAuthenticators(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ supports both biometric and device credential
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else {
            // Android 7-10 use biometric only
            BIOMETRIC_WEAK
        }
    }

    /**
     * Check if device credential (PIN/Pattern/Password) is set
     */
    fun isDeviceSecure(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            biometricManager.canAuthenticate(DEVICE_CREDENTIAL) ==
                    BiometricManager.BIOMETRIC_SUCCESS
        } else {
            // For older versions, check if biometric is available
            biometricManager.canAuthenticate(BIOMETRIC_WEAK) ==
                    BiometricManager.BIOMETRIC_SUCCESS
        }
    }
}