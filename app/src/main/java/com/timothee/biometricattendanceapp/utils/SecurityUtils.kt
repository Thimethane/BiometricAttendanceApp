package com.timothee.biometricattendance.utils

import android.os.Build
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object SecurityUtils {

    /**
     * Generate a random salt for password hashing
     */
    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getEncoder().encodeToString(salt)
        } else {
            Base64.encodeToString(salt, Base64.DEFAULT)
        }
    }

    /**
     * Hash password with salt using SHA-256
     */
    fun hashPassword(password: String, salt: String): String {
        val saltedPassword = password + salt
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(saltedPassword.toByteArray(Charsets.UTF_8))
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getEncoder().encodeToString(hash)
        } else {
            Base64.encodeToString(hash, Base64.DEFAULT)
        }
    }

    /**
     * Verify password against stored hash and salt
     */
    fun verifyPassword(password: String, storedHash: String, salt: String): Boolean {
        val computedHash = hashPassword(password, salt)
        return computedHash.trim() == storedHash.trim()
    }
}