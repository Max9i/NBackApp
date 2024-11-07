package com.emmajson.nbackapp.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emmajson.nbackapp.ui.screens.HomeScreen
import com.emmajson.nbackapp.ui.screens.NBackScreen
import com.emmajson.nbackapp.ui.screens.SettingsScreen
import com.emmajson.nbackapp.ui.viewmodels.GameViewModel

@Composable
fun Navigation(vm: GameViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            Crossfade(targetState = Screen.HomeScreen) {
                HomeScreen(vm, navController = navController)
            }
        }
        composable(route = Screen.NBackScreen.route) {
            Crossfade(targetState = Screen.NBackScreen) {
                NBackScreen(vm, navController = navController)
            }
        }
        composable(route = Screen.SettingsScreen.route) {
            Crossfade(targetState = Screen.SettingsScreen) {
                SettingsScreen(vm, navController = navController)
            }
        }
    }
}