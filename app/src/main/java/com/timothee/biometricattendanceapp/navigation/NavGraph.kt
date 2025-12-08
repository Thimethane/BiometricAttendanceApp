package com.timothee.biometricattendance.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.timothee.biometricattendance.ui.auth.SetPasswordScreen
import com.timothee.biometricattendance.ui.auth.SignInScreen
import com.timothee.biometricattendance.ui.auth.SignUpScreen
import com.timothee.biometricattendance.ui.home.HomeScreen
import com.timothee.biometricattendance.ui.attendance.AttendanceHistoryScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToSetPassword = { email, name ->
                    navController.navigate(Screen.SetPassword.createRoute(email, name))
                },
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.SetPassword.route,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""

            SetPasswordScreen(
                email = email,
                name = name,
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAttendanceHistory = {
                    navController.navigate(Screen.AttendanceHistory.route)
                },
                onLogout = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AttendanceHistory.route) {
            AttendanceHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
