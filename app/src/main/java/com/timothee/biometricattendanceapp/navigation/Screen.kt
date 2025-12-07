package com.timothee.biometricattendance.navigation

sealed class Screen(val route: String) {
    object SignUp : Screen("signup")
    object SetPassword : Screen("set_password/{email}/{name}") {
        fun createRoute(email: String, name: String) = "set_password/$email/$name"
    }
    object SignIn : Screen("signin")
    object Home : Screen("home")
    object AttendanceHistory : Screen("attendance_history")
}