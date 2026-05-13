package com.jeiel85.wildhavenidle.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F7D57),
    onPrimary = Color.White,
    secondary = Color(0xFF376A7A),
    tertiary = Color(0xFF8A6A32),
    background = Color(0xFFF7F9F4),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF17201B),
    onSurface = Color(0xFF17201B),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF79D0A2),
    onPrimary = Color(0xFF08351F),
    secondary = Color(0xFF86C7D7),
    tertiary = Color(0xFFD5B56D),
    background = Color(0xFF101612),
    surface = Color(0xFF19221C),
    onBackground = Color(0xFFE6EFE7),
    onSurface = Color(0xFFE6EFE7),
)

@Composable
fun WildHavenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme: ColorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
