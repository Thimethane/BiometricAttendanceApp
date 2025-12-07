package com.timothee.biometricattendance.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SignUpScreen(
    onNavigateToSetPassword: (String, String) -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Sign Up Screen", style = MaterialTheme.typography.headlineMedium)
            Text("(Full implementation coming next)")
            Button(onClick = onNavigateToSignIn) {
                Text("Go to Sign In")
            }
        }
    }
}

@Composable
fun SetPasswordScreen(
    email: String,
    name: String,
    onNavigateToSignIn: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Set Password Screen", style = MaterialTheme.typography.headlineMedium)
            Text("Email: $email")
            Text("Name: $name")
            Button(onClick = onNavigateToSignIn) {
                Text("Continue to Sign In")
            }
        }
    }
}

@Composable
fun SignInScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Sign In Screen", style = MaterialTheme.typography.headlineMedium)
            Text("(Full implementation coming next)")
            Button(onClick = onNavigateToHome) {
                Text("Go to Home")
            }
            TextButton(onClick = onNavigateToSignUp) {
                Text("Don't have account? Sign Up")
            }
        }
    }
}