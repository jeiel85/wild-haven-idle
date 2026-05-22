package com.jeiel85.wildhavenidle.renew.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ArtisticSage,
    secondary = ArtisticOlive,
    tertiary = ArtisticTerraCotta,
    background = PrimaryDarkMoss,
    surface = Color(0xFF1B2618),
    onPrimary = PrimaryDarkMoss,
    onSecondary = Color.White,
    onBackground = ArtisticIvory,
    onSurface = ArtisticIvory
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ArtisticOlive,
    secondary = ArtisticSage,
    tertiary = ArtisticTerraCotta,
    background = ArtisticIvory,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = ArtisticCharcoal,
    onBackground = ArtisticCharcoal,
    onSurface = ArtisticCharcoal,
    surfaceVariant = ArtisticEarthySand,
    onSurfaceVariant = ArtisticCharcoal
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color so we preserve real Artistic custom brand colors
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
