package com.timothee.biometricattendanceapp.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.timothee.biometricattendanceapp.data.local.database.AppDatabase
import com.timothee.biometricattendanceapp.data.local.entities.Attendance
import com.timothee.biometricattendanceapp.data.local.entities.User
import com.timothee.biometricattendanceapp.data.repository.AttendanceRepository
import com.timothee.biometricattendanceapp.data.repository.UserRepository
import com.timothee.biometricattendanceapp.utils.Constants
import com.timothee.biometricattendanceapp.utils.DateTimeUtils
import com.timothee.biometricattendanceapp.utils.LocationUtils
import com.timothee.biometricattendanceapp.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val attendanceRepository: AttendanceRepository
    private val sessionManager: SessionManager

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
        attendanceRepository = AttendanceRepository(database.attendanceDao())
        sessionManager = SessionManager(application)
    }

    // User State
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Check-In State
    private val _checkInState = MutableStateFlow<CheckInOutState>(CheckInOutState.Idle)
    val checkInState: StateFlow<CheckInOutState> = _checkInState.asStateFlow()

    // Check-Out State
    private val _checkOutState = MutableStateFlow<CheckInOutState>(CheckInOutState.Idle)
    val checkOutState: StateFlow<CheckInOutState> = _checkOutState.asStateFlow()

    // Today's Attendance
    private val _todayAttendance = MutableStateFlow<Attendance?>(null)
    val todayAttendance: StateFlow<Attendance?> = _todayAttendance.asStateFlow()

    /**
     * Load current user
     */
    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    val user = userRepository.getUserById(userId)
                    _currentUser.value = user
                    if (user != null) {
                        loadTodayAttendance(userId)
                    }
                }
            } catch (e: Exception) {
                // Handle error silently or log
                e.printStackTrace()
            }
        }
    }

    /**
     * Load today's attendance
     */
    private fun loadTodayAttendance(userId: Int) {
        viewModelScope.launch {
            try {
                val today = DateTimeUtils.getCurrentDate()
                val attendance = attendanceRepository.getAttendanceByDate(userId, today)
                _todayAttendance.value = attendance
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Check if biometric is registered
     */
    fun isBiometricRegistered(): Boolean {
        return _currentUser.value?.isBiometricRegistered ?: false
    }

    /**
     * Register biometric
     */
    fun registerBiometric() {
        viewModelScope.launch {
            try {
                val user = _currentUser.value ?: return@launch
                userRepository.updateBiometricStatus(user.id, true)
                _currentUser.value = user.copy(isBiometricRegistered = true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Process check-in
     */
    fun checkIn(location: Location?) {
        viewModelScope.launch {
            try {
                _checkInState.value = CheckInOutState.Loading

                val user = _currentUser.value
                if (user == null) {
                    _checkInState.value = CheckInOutState.Error("User not found")
                    return@launch
                }

                val today = DateTimeUtils.getCurrentDate()

                // Check if already checked in today
                val hasCheckedIn = attendanceRepository.hasCheckedInToday(user.id, today)
                if (hasCheckedIn) {
                    _checkInState.value = CheckInOutState.Error("You have already checked in today")
                    return@launch
                }

                // Validate location
                if (location == null) {
                    _checkInState.value = CheckInOutState.Error("Unable to get location. Please enable GPS and try again")
                    return@launch
                }

                // Check location accuracy
                if (location.accuracy > Constants.GPS_ACCURACY_THRESHOLD) {
                    _checkInState.value = CheckInOutState.Error(
                        "GPS accuracy is too low (${location.accuracy.toInt()}m). Please wait for better signal"
                    )
                    return@launch
                }

                // Check if within office premises
                val isWithinOffice = LocationUtils.isWithinOfficePremises(
                    location.latitude,
                    location.longitude
                )

                if (!isWithinOffice) {
                    val distance = LocationUtils.calculateDistance(
                        location.latitude,
                        location.longitude,
                        Constants.OFFICE_LATITUDE,
                        Constants.OFFICE_LONGITUDE
                    )
                    _checkInState.value = CheckInOutState.Error(
                        "You are not at office premises. Distance: ${LocationUtils.formatDistance(distance)} from office"
                    )
                    return@launch
                }

                // Create attendance record
                val attendance = Attendance(
                    userId = user.id,
                    checkInTime = System.currentTimeMillis(),
                    checkOutTime = null,
                    date = today,
                    checkInLatitude = location.latitude,
                    checkInLongitude = location.longitude
                )

                val attendanceId = attendanceRepository.insertAttendance(attendance)

                if (attendanceId > 0) {
                    _todayAttendance.value = attendance.copy(id = attendanceId.toInt())
                    _checkInState.value = CheckInOutState.Success(
                        "Checked in successfully at ${DateTimeUtils.getCurrentTime()}"
                    )
                } else {
                    _checkInState.value = CheckInOutState.Error("Failed to check in. Please try again")
                }

            } catch (e: Exception) {
                _checkInState.value = CheckInOutState.Error(
                    "Error: ${e.localizedMessage ?: "Unknown error occurred"}"
                )
                e.printStackTrace()
            }
        }
    }

    /**
     * Process check-out
     */
    fun checkOut(location: Location?) {
        viewModelScope.launch {
            try {
                _checkOutState.value = CheckInOutState.Loading

                val user = _currentUser.value
                if (user == null) {
                    _checkOutState.value = CheckInOutState.Error("User not found")
                    return@launch
                }

                val today = DateTimeUtils.getCurrentDate()

                // Check if checked in today
                val hasCheckedIn = attendanceRepository.hasCheckedInToday(user.id, today)
                if (!hasCheckedIn) {
                    _checkOutState.value = CheckInOutState.Error("You must check in before checking out")
                    return@launch
                }

                // Check if already checked out today
                val hasCheckedOut = attendanceRepository.hasCheckedOutToday(user.id, today)
                if (hasCheckedOut) {
                    _checkOutState.value = CheckInOutState.Error("You have already checked out today")
                    return@launch
                }

                // Validate location
                if (location == null) {
                    _checkOutState.value = CheckInOutState.Error("Unable to get location. Please enable GPS and try again")
                    return@launch
                }

                // Check if within office premises
                val isWithinOffice = LocationUtils.isWithinOfficePremises(
                    location.latitude,
                    location.longitude
                )

                if (!isWithinOffice) {
                    val distance = LocationUtils.calculateDistance(
                        location.latitude,
                        location.longitude,
                        Constants.OFFICE_LATITUDE,
                        Constants.OFFICE_LONGITUDE
                    )
                    _checkOutState.value = CheckInOutState.Error(
                        "You are not at office premises. Distance: ${LocationUtils.formatDistance(distance)} from office"
                    )
                    return@launch
                }

                // Get today's attendance
                val attendance = attendanceRepository.getAttendanceByDate(user.id, today)
                if (attendance == null) {
                    _checkOutState.value = CheckInOutState.Error("Attendance record not found")
                    return@launch
                }

                // Update attendance with check-out
                val updatedAttendance = attendance.copy(
                    checkOutTime = System.currentTimeMillis(),
                    checkOutLatitude = location.latitude,
                    checkOutLongitude = location.longitude
                )

                attendanceRepository.updateAttendance(updatedAttendance)

                // Calculate duration
                val duration = DateTimeUtils.calculateDuration(
                    attendance.checkInTime ?: 0,
                    updatedAttendance.checkOutTime ?: 0
                )

                _todayAttendance.value = updatedAttendance
                _checkOutState.value = CheckInOutState.Success(
                    "Checked out successfully at ${DateTimeUtils.getCurrentTime()}. Duration: $duration"
                )

            } catch (e: Exception) {
                _checkOutState.value = CheckInOutState.Error(
                    "Error: ${e.localizedMessage ?: "Unknown error occurred"}"
                )
                e.printStackTrace()
            }
        }
    }

    /**
     * Logout user
     */
    fun logout() {
        sessionManager.clearSession()
    }

    /**
     * Reset check-in state
     */
    fun resetCheckInState() {
        _checkInState.value = CheckInOutState.Idle
    }

    /**
     * Reset check-out state
     */
    fun resetCheckOutState() {
        _checkOutState.value = CheckInOutState.Idle
    }
}

/**
 * Check-In/Out State sealed class
 */
sealed class CheckInOutState {
    object Idle : CheckInOutState()
    object Loading : CheckInOutState()
    data class Success(val message: String) : CheckInOutState()
    data class Error(val message: String) : CheckInOutState()
}
