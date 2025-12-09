package com.timothee.biometricattendanceapp.data.local.dao

import androidx.room.*
import com.timothee.biometricattendanceapp.data.local.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmailFlow(email: String): Flow<User?>

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET isBiometricRegistered = :isRegistered WHERE id = :userId")
    suspend fun updateBiometricStatus(userId: Int, isRegistered: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Delete
    suspend fun deleteUser(user: User)
}