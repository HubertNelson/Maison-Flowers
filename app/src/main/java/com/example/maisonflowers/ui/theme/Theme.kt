package com.example.maisonflowers.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFFFFBFE) // Color del texto sobre el fondo oscuro
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonAccentPurple, // Usamos el morado del botón como primary (color principal de interacción)
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = BackgroundLightPurple, // Fondo morado claro para el Light Theme
    onBackground = TextDarkPurple,      // Texto morado oscuro sobre el fondo claro
    surface = BackgroundLightPurple,    // Para superficies, usamos el mismo color de fondo
    onPrimary = Color.White,            // Color del texto sobre el botón primary
    onSecondary = Color.White,
    onTertiary = Color.White,
    onSurface = TextDarkPurple          // Color del texto sobre la superficie
)

@Composable
fun MaisonFlowersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
