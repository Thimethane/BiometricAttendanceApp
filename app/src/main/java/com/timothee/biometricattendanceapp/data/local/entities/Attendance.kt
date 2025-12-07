package com.timothee.biometricattendance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val checkInTime: Long? = null,
    val checkOutTime: Long? = null,
    val date: String, // Format: "yyyy-MM-dd"
    val checkInLatitude: Double? = null,
    val checkInLongitude: Double? = null,
    val checkOutLatitude: Double? = null,
    val checkOutLongitude: Double? = null
)