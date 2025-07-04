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
    background = Color(0xFF1C1B1F), // Fondo oscuro predeterminado de Material3
    onBackground = Color(0xFFFFFBFE), // Color del texto sobre el fondo oscuro
    surface = Color(0xFF1C1B1F),       // Superficie oscura predeterminada
    onSurface = Color(0xFFFFFBFE)      // Color del texto sobre la superficie oscura
)

private val LightColorScheme = lightColorScheme(
    primary = TextDarkPurple,       // Ahora el morado oscuro es el color principal para interacciones
    onPrimary = Color.White,        // Texto blanco sobre el color primary

    secondary = PurpleGrey40,
    onSecondary = Color.White,

    tertiary = Pink40,
    onTertiary = Color.White,

    background = ButtonAccentPurple, // ¡Ahora tu morado vibrante es el color de fondo!
    onBackground = Color.White,      // Texto blanco sobre el fondo vibrante para mejor contraste

    surface = ButtonAccentPurple,    // Usamos el mismo color de fondo para las superficies (cards, etc.)
    onSurface = Color.White          // Texto blanco sobre las superficies vibrantes
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
        typography = Typography, // Asegúrate de que Typography esté definido en tu proyecto
        content = content
    )
}
