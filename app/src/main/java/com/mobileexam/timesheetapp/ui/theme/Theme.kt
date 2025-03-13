package com.mobileexam.timesheetapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define the Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1E88E5),  // Blue color for buttons and highlights
    secondary = Color(0xFF0A0E21), // Dark background
    background = Color(0xFF0A0E21), // Background color
    surface = Color(0xFF121212), // Darker surface
    onPrimary = Color.White, // Text color on primary elements
    onSecondary = Color.LightGray, // Text color on secondary elements
    onBackground = Color.White, // Default text color
    onSurface = Color.White // Text on surfaces
)

// Define the Light Color Scheme (if needed)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),
    secondary = Color.White,
    background = Color.White,
    surface = Color(0xFFF0F0F0),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Theme function that applies the colors
@Composable
fun JairosoftLoginTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
