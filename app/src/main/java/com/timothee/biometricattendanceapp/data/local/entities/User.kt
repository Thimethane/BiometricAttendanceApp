package com.timothee.biometricattendance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    val isBiometricRegistered: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)