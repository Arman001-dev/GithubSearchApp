package com.githubsearchapp.presentation.navigation.utils

import androidx.navigation.NavHostController

fun NavHostController.navigateAndClearBackStack(route: String) {
    this.navigate(route) {
        popUpTo(0) {
            inclusive = true
        }
    }
}