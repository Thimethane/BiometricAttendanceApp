package com.timothee.biometricattendanceapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.timothee.biometricattendanceapp.data.local.database.AppDatabase
import com.timothee.biometricattendanceapp.data.local.entities.User
import com.timothee.biometricattendanceapp.data.repository.UserRepository
import com.timothee.biometricattendanceapp.utils.SecurityUtils
import com.timothee.biometricattendanceapp.utils.SessionManager
import com.timothee.biometricattendanceapp.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val sessionManager: SessionManager

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        sessionManager = SessionManager(application)
    }

    // Sign Up State
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    // Sign In State
    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    // Validation States
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    /**
     * Validate email format
     */
    fun validateEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                _emailError.value = "Email cannot be empty"
                false
            }
            !ValidationUtils.isValidEmail(email) -> {
                _emailError.value = "Please enter a valid email address"
                false
            }
            else -> {
                _emailError.value = null
                true
            }
        }
    }

    /**
     * Validate name
     */
    fun validateName(name: String): Boolean {
        return when {
            name.isBlank() -> {
                _nameError.value = "Name cannot be empty"
                false
            }
            !ValidationUtils.isValidName(name) -> {
                _nameError.value = "Name must be at least 2 characters"
                false
            }
            else -> {
                _nameError.value = null
                true
            }
        }
    }

    /**
     * Validate password
     */
    fun validatePassword(password: String): Boolean {
        val error = ValidationUtils.getPasswordError(password)
        _passwordError.value = error
        return error == null
    }

    /**
     * Check if email already exists
     */
    suspend fun checkEmailExists(email: String): Boolean {
        return userRepository.isEmailExists(email)
    }

    /**
     * Sign up new user
     */
    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _signUpState.value = SignUpState.Loading

                // Validate inputs
                if (!validateName(name) || !validateEmail(email) || !validatePassword(password)) {
                    _signUpState.value = SignUpState.Error("Please check all fields")
                    return@launch
                }

                // Check if email already exists
                if (checkEmailExists(email)) {
                    _signUpState.value = SignUpState.Error("Email already registered")
                    return@launch
                }

                // Generate salt and hash password
                val salt = SecurityUtils.generateSalt()
                val passwordHash = SecurityUtils.hashPassword(password, salt)

                // Create user
                val user = User(
                    name = name.trim(),
                    email = email.trim().lowercase(),
                    passwordHash = passwordHash,
                    passwordSalt = salt,
                    isBiometricRegistered = false
                )

                // Insert into database
                val userId = userRepository.insertUser(user)

                if (userId > 0) {
                    _signUpState.value = SignUpState.Success
                } else {
                    _signUpState.value = SignUpState.Error("Failed to create account")
                }

            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Sign in existing user
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _signInState.value = SignInState.Loading

                // Validate inputs
                if (email.isBlank() || password.isBlank()) {
                    _signInState.value = SignInState.Error("Please enter email and password")
                    return@launch
                }

                // Get user from database
                val user = userRepository.getUserByEmail(email.trim().lowercase())

                if (user == null) {
                    _signInState.value = SignInState.Error("Invalid email or password")
                    return@launch
                }

                // Verify password
                val isPasswordValid = SecurityUtils.verifyPassword(
                    password = password,
                    storedHash = user.passwordHash,
                    salt = user.passwordSalt
                )

                if (!isPasswordValid) {
                    _signInState.value = SignInState.Error("Invalid email or password")
                    return@launch
                }

                // Save session
                sessionManager.saveLoginSession(user.id, user.email)

                _signInState.value = SignInState.Success(user)

            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Reset sign up state
     */
    fun resetSignUpState() {
        _signUpState.value = SignUpState.Idle
    }

    /**
     * Reset sign in state
     */
    fun resetSignInState() {
        _signInState.value = SignInState.Idle
    }

    /**
     * Clear all errors
     */
    fun clearErrors() {
        _emailError.value = null
        _nameError.value = null
        _passwordError.value = null
    }
}

/**
 * Sign Up State sealed class
 */
sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}

/**
 * Sign In State sealed class
 */
sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Success(val user: User) : SignInState()
    data class Error(val message: String) : SignInState()
}
