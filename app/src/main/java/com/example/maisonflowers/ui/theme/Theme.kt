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

// Esquema de colores para el TEMA OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = Purple80, // Un color principal para el modo oscuro
    onPrimary = Color.Black, // Texto sobre primary en oscuro
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212), // Un gris muy oscuro para el fondo del modo oscuro
    onBackground = Color(0xFFE0E0E0), // Texto claro sobre el fondo oscuro
    surface = Color(0xFF1E1E1E),     // Superficies (cards, etc.) un poco más claras que el fondo
    onSurface = Color(0xFFE0E0E0),   // Texto sobre superficies oscuras
    error = Color(0xFFCF6679),
    onError = Color.Black,
    surfaceVariant = Color(0xFF424242), // Un tono más oscuro para variantes de superficie en modo oscuro
    onSurfaceVariant = Color(0xFFBDBDBD) // Texto sobre variantes de superficie en modo oscuro
)

// Esquema de colores para el TEMA CLARO
private val LightColorScheme = lightColorScheme(
    primary = ButtonAccentPurple, // Tu morado vibrante para el color principal de interacción (botones, etc.)
    onPrimary = Color.White,      // Texto blanco sobre el color primary

    secondary = PurpleGrey40,
    onSecondary = Color.White,

    tertiary = Pink40,
    onTertiary = Color.White,

    background = BackgroundLightPurple, // Tu color morado claro (0xFFE9C9F6) para el fondo principal
    onBackground = TextDarkPurple,      // Tu morado oscuro para el texto sobre el fondo claro

    surface = Color.White,              // ¡CAMBIO AQUÍ! El fondo de las superficies (incluidos los buscadores) ahora es blanco en modo claro.
    onSurface = TextDarkPurple,         // Tu morado oscuro para el texto sobre las superficies blancas
    error = Color(0xFFB00020),
    onError = Color.White,
    surfaceVariant = Color(0xFFF0F0F0), // Un tono más claro para variantes de superficie en modo claro
    onSurfaceVariant = TextDarkPurple.copy(alpha = 0.7f) // Texto más suave para variantes de superficie (placeholders)
)

@Composable
fun MaisonFlowersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
