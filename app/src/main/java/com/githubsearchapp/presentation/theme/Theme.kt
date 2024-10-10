package com.githubsearchapp.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = color6,
    onBackground = color1,
    primary = color2,
    onPrimary = color1,
    secondary = color7,
    onSecondary = color1,
    tertiary = color3,
    error = color9,
    onError = color1
)

@Composable
fun GithubSearchAppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}