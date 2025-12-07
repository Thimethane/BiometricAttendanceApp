package com.timothee.biometricattendance.data.repository

import com.timothee.biometricattendance.data.local.dao.UserDao
import com.timothee.biometricattendance.data.local.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    fun getUserByEmailFlow(email: String): Flow<User?> {
        return userDao.getUserByEmailFlow(email)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateBiometricStatus(userId: Int, isRegistered: Boolean) {
        userDao.updateBiometricStatus(userId, isRegistered)
    }

    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}