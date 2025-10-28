package com.gymnastic.ecommerceapp.ui.theme

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

// Esquema de colores para tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Blue80, // Azul principal
    secondary = Orange80, // Naranja secundario
    tertiary = Grey80, // Gris terciario
    background = Color(0xFF121212), // Fondo oscuro
    surface = Color(0xFF1E1E1E), // Superficie oscura
    onPrimary = Color.Black, // Texto sobre azul
    onSecondary = Color.Black, // Texto sobre naranja
    onTertiary = Color.Black, // Texto sobre gris
    onBackground = Color.White, // Texto sobre fondo
    onSurface = Color.White // Texto sobre superficie
)

// Esquema de colores para tema claro
private val LightColorScheme = lightColorScheme(
    primary = Blue40, // Azul principal
    secondary = Orange40, // Naranja secundario
    tertiary = Grey40, // Gris terciario
    background = Color(0xFFFFFBFE), // Fondo claro
    surface = Color(0xFFFFFBFE), // Superficie clara
    onPrimary = Color.White, // Texto sobre azul
    onSecondary = Color.White, // Texto sobre naranja
    onTertiary = Color.White, // Texto sobre gris
    onBackground = Color(0xFF1C1B1F), // Texto sobre fondo
    onSurface = Color(0xFF1C1B1F) // Texto sobre superficie
)

@Composable
fun EcommerceappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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