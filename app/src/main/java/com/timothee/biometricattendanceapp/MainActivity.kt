package com.timothee.biometricattendanceapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.timothee.biometricattendanceapp.navigation.NavGraph
import com.timothee.biometricattendanceapp.navigation.Screen
import com.timothee.biometricattendanceapp.ui.theme.BiometricAttendanceAppTheme
import com.timothee.biometricattendanceapp.utils.SessionManager
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize session manager
        sessionManager = SessionManager(this)

        enableEdgeToEdge()

        setContent {
            BiometricAttendanceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Determine start destination based on login status
                    val startDestination = if (sessionManager.isLoggedIn()) {
                        Screen.Home.route
                    } else {
                        Screen.SignIn.route
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}