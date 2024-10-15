package com.githubsearchapp.presentation.navigation.routes

sealed class Screens(val route: String) {

    data object Home : Screens("Home")
    data object DownloadedRepositories : Screens("DownloadedRepositories")

    fun withArgs(vararg args: String?): String {
        return buildString {
            append(route)
            args.forEach { args ->
                append("/$args")
            }
        }
    }

    fun withArgsPath(vararg args: String?): String {
        return buildString {
            append("{$route}")
            args.forEach { args ->
                append("/{$args}")
            }
        }
    }
}