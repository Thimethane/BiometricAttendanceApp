package com.timothee.biometricattendanceapp.data.repository

import com.timothee.biometricattendanceapp.data.local.dao.AttendanceDao
import com.timothee.biometricattendanceapp.data.local.entities.Attendance
import kotlinx.coroutines.flow.Flow

class AttendanceRepository(private val attendanceDao: AttendanceDao) {

    suspend fun insertAttendance(attendance: Attendance): Long {
        return attendanceDao.insertAttendance(attendance)
    }

    suspend fun updateAttendance(attendance: Attendance) {
        attendanceDao.updateAttendance(attendance)
    }

    fun getAllAttendanceByUser(userId: Int): Flow<List<Attendance>> {
        return attendanceDao.getAllAttendanceByUser(userId)
    }

    suspend fun getAttendanceByDate(userId: Int, date: String): Attendance? {
        return attendanceDao.getAttendanceByDate(userId, date)
    }

    suspend fun hasCheckedInToday(userId: Int, date: String): Boolean {
        return attendanceDao.hasCheckedInToday(userId, date)
    }

    suspend fun hasCheckedOutToday(userId: Int, date: String): Boolean {
        return attendanceDao.hasCheckedOutToday(userId, date)
    }

    suspend fun deleteAttendance(attendance: Attendance) {
        attendanceDao.deleteAttendance(attendance)
    }

    suspend fun deleteAllAttendanceByUser(userId: Int) {
        attendanceDao.deleteAllAttendanceByUser(userId)
    }
}