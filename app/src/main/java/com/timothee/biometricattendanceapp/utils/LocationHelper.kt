package com.timothee.biometricattendance.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * A helper utility to manage all location-related operations:
 * - Permission checks
 * - Current location updates via Flow
 * - Last known location retrieval
 *
 * Designed to work cleanly with Kotlin coroutines & Jetpack Compose.
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Check if both FINE and COARSE location permissions are granted.
     */
    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine && coarse
    }

    /**
     * Stream location updates as a Flow<Location?>.
     * Emits:
     * - Latest high-accuracy location every few seconds
     * - null if permissions are missing
     */
    fun getCurrentLocation(): Flow<Location?> = callbackFlow {
        // Permission check first
        if (!hasLocationPermission()) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L // request interval
        )
            .setMinUpdateIntervalMillis(5_000L)
            .setMaxUpdateDelayMillis(10_000L)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation
                trySend(loc) // emits null if loc = null safely
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )
        } catch (se: SecurityException) {
            trySend(null)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    /**
     * Retrieve the last known location (may be cached).
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLastLocation(): Location? {
        if (!hasLocationPermission()) return null

        return try {
            fusedLocationClient.lastLocation.await()
        } catch (_: Exception) {
            null
        }
    }
}
