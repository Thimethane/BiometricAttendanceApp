package com.timothee.biometricattendanceapp.data.local.dao

import androidx.room.*
import com.timothee.biometricattendanceapp.data.local.entities.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance): Long

    @Update
    suspend fun updateAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE userId = :userId ORDER BY date DESC, checkInTime DESC")
    fun getAllAttendanceByUser(userId: Int): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE userId = :userId AND date = :date LIMIT 1")
    suspend fun getAttendanceByDate(userId: Int, date: String): Attendance?

    @Query("SELECT EXISTS(SELECT 1 FROM attendance WHERE userId = :userId AND date = :date AND checkInTime IS NOT NULL)")
    suspend fun hasCheckedInToday(userId: Int, date: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM attendance WHERE userId = :userId AND date = :date AND checkOutTime IS NOT NULL)")
    suspend fun hasCheckedOutToday(userId: Int, date: String): Boolean

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    @Query("DELETE FROM attendance WHERE userId = :userId")
    suspend fun deleteAllAttendanceByUser(userId: Int)
}