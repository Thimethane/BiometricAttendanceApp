package com.timothee.biometricattendance.utils

object Constants {
    // Minimum Android Version Info
    const val MIN_SDK_VERSION = 24 // Android 7.0

    // Office GPS Coordinates (Update with your office location)
    const val OFFICE_LATITUDE = -1.9441  // Kigali, Rwanda example
    const val OFFICE_LONGITUDE = 30.0619
    const val OFFICE_RADIUS_METERS = 200.0 // 200 meters radius

    // Location accuracy threshold
    const val GPS_ACCURACY_THRESHOLD = 50.0 // meters

    // Session management
    const val PREF_NAME = "biometric_attendance_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_EMAIL = "user_email"

    // Date format
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val TIME_FORMAT = "hh:mm a"
    const val DATETIME_FORMAT = "dd MMM yyyy, hh:mm a"

    // Password requirements
    const val MIN_PASSWORD_LENGTH = 8

    // Biometric
    const val MAX_BIOMETRIC_RETRIES = 3
}