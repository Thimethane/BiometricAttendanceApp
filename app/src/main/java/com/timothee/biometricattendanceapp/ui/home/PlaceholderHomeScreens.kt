package com.timothee.biometricattendance.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToAttendanceHistory: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
            Text("(Check-in/Check-out functionality coming next)")
            Button(onClick = onNavigateToAttendanceHistory) {
                Text("View Attendance History")
            }
            OutlinedButton(onClick = onLogout) {
                Text("Logout")
            }
        }
    }
}