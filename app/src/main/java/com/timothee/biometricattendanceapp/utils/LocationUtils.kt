package com.timothee.biometricattendance.utils

import android.location.Location
import kotlin.math.*

object LocationUtils {

    /**
     * Calculate distance between two GPS coordinates using Haversine formula
     * Returns distance in meters
     */
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadius = 6371000.0 // Earth radius in meters

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    /**
     * Check if user is within office premises
     */
    fun isWithinOfficePremises(
        userLat: Double,
        userLon: Double,
        officeLat: Double = Constants.OFFICE_LATITUDE,
        officeLon: Double = Constants.OFFICE_LONGITUDE,
        radiusMeters: Double = Constants.OFFICE_RADIUS_METERS
    ): Boolean {
        val distance = calculateDistance(userLat, userLon, officeLat, officeLon)
        return distance <= radiusMeters
    }

    /**
     * Check if location accuracy is acceptable
     */
    fun isLocationAccurate(accuracy: Float): Boolean {
        return accuracy <= Constants.GPS_ACCURACY_THRESHOLD
    }

    /**
     * Format distance for display
     */
    fun formatDistance(distanceMeters: Double): String {
        return if (distanceMeters < 1000) {
            "${distanceMeters.toInt()} m"
        } else {
            String.format("%.2f km", distanceMeters / 1000)
        }
    }
}