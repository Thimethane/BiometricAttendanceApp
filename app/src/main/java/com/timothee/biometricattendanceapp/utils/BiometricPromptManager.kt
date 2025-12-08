package com.timothee.biometricattendance.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricPromptManager(
    private val activity: FragmentActivity
) {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    /**
     * Show biometric prompt for authentication
     */
    fun showBiometricPrompt(
        title: String,
        subtitle: String,
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        // Check if biometric is available
        val biometricManager = BiometricManager.from(activity)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Biometric is available, continue
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                onError("No biometric hardware available on this device")
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                onError("Biometric hardware is currently unavailable")
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onError("No biometric credentials enrolled. Please enroll in device settings")
                return
            }
            else -> {
                onError("Biometric authentication not available")
                return
            }
        }

        // Create biometric prompt callback
        val executor = ContextCompat.getMainExecutor(activity)

        biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                        onError("Authentication cancelled")
                    } else {
                        onError("Authentication error: $errString")
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )

        // Build prompt info
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        // Show prompt
        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * Show biometric registration prompt
     */
    fun showBiometricRegistration(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        showBiometricPrompt(
            title = "Register Biometric",
            subtitle = "Scan your fingerprint or use face unlock to register",
            onSuccess = onSuccess,
            onError = onError,
            onFailed = {
                onError("Biometric not recognized. Please try again")
            }
        )
    }

    /**
     * Show biometric prompt for check-in
     */
    fun authenticateForCheckIn(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        showBiometricPrompt(
            title = "Check In",
            subtitle = "Authenticate to check in",
            onSuccess = onSuccess,
            onError = onError,
            onFailed = {
                onError("Biometric not recognized. Please try again")
            }
        )
    }

    /**
     * Show biometric prompt for check-out
     */
    fun authenticateForCheckOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        showBiometricPrompt(
            title = "Check Out",
            subtitle = "Authenticate to check out",
            onSuccess = onSuccess,
            onError = onError,
            onFailed = {
                onError("Biometric not recognized. Please try again")
            }
        )
    }
}