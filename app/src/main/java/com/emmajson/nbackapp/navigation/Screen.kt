package com.emmajson.nbackapp.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object NBackScreen : Screen("nback_screen")
    object SettingsScreen : Screen("settings_screen")
}