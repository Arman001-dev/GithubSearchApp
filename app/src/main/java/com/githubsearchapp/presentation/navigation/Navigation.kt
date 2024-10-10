package com.githubsearchapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.githubsearchapp.presentation.navigation.routes.Screens
import com.githubsearchapp.presentation.home.HomeScreen

@Composable
fun GitReposSearchNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(route = Screens.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}