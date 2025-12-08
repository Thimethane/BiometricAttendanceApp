package com.timothee.biometricattendance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.timothee.biometricattendance.data.local.database.AppDatabase
import com.timothee.biometricattendance.data.local.entities.Attendance
import com.timothee.biometricattendance.data.repository.AttendanceRepository
import com.timothee.biometricattendance.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val attendanceRepository: AttendanceRepository
    private val sessionManager: SessionManager

    init {
        val attendanceDao = AppDatabase.getDatabase(application).attendanceDao()
        attendanceRepository = AttendanceRepository(attendanceDao)
        sessionManager = SessionManager(application)
    }

    // Attendance List State
    private val _attendanceList = MutableStateFlow<List<Attendance>>(emptyList())
    val attendanceList: StateFlow<List<Attendance>> = _attendanceList.asStateFlow()

    // Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error State
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Load all attendance records for current user
     */
    fun loadAttendanceHistory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val userId = sessionManager.getUserId()
                if (userId == -1) {
                    _error.value = "User not logged in"
                    _isLoading.value = false
                    return@launch
                }

                attendanceRepository.getAllAttendanceByUser(userId).collect { attendanceList ->
                    _attendanceList.value = attendanceList
                    _isLoading.value = false
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load attendance"
                _isLoading.value = false
            }
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        _error.value = null
    }
}