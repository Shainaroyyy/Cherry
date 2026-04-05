package com.example.cherry.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CherryColorScheme = darkColorScheme(
    primary = Color(0xFF640D14),
    onPrimary = Color(0xFFF5E0E2),
    primaryContainer = Color(0xFF38040E),
    onPrimaryContainer = Color(0xFFF5E0E2),
    secondary = Color(0xFFAD777B),
    onSecondary = Color(0xFF250902),
    background = Color(0xFF250902),
    onBackground = Color(0xFFF5E0E2),
    surface = Color(0xFF38040E),
    onSurface = Color(0xFFF5E0E2),
    surfaceVariant = Color(0xFF640D14),
    onSurfaceVariant = Color(0xFFAD777B),
)

@Composable
fun CherryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CherryColorScheme,
        typography = Typography,
        content = content
    )
}