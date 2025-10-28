package com.gymnastic.ecommerceapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Tema profesional inspirado en shadcn/ui
 *
 * Características:
 * - Paleta neutral zinc para máxima legibilidad
 * - Acento azul slate profesional
 * - Colores semánticos para estados
 * - Diseñado para ser elegante y accesible
 */

// ========== TEMA CLARO (Default) ==========
private val LightColorScheme = lightColorScheme(
    // Colores primarios - Azul profesional
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Zinc100,
    onPrimaryContainer = Blue800,

    // Colores secundarios - Slate neutro
    secondary = Slate600,
    onSecondary = White,
    secondaryContainer = Zinc100,
    onSecondaryContainer = Slate900,

    // Colores terciarios - Acento sutil
    tertiary = Slate500,
    onTertiary = White,
    tertiaryContainer = Zinc50,
    onTertiaryContainer = Slate800,

    // Colores de error
    error = Red600,
    onError = White,
    errorContainer = Red50,
    onErrorContainer = Red700,

    // Fondos y superficies
    background = White,
    onBackground = Zinc900,
    surface = White,
    onSurface = Zinc900,
    surfaceVariant = Zinc50,
    onSurfaceVariant = Zinc600,

    // Bordes y divisores
    outline = Zinc300,
    outlineVariant = Zinc200,

    // Otros
    scrim = BlackAlpha20,
    inverseSurface = Zinc900,
    inverseOnSurface = Zinc50,
    inversePrimary = Blue400,
    surfaceTint = Blue600
)

// ========== TEMA OSCURO ==========
private val DarkColorScheme = darkColorScheme(
    // Colores primarios - Azul profesional
    primary = Blue500,
    onPrimary = Zinc900,
    primaryContainer = Blue800,
    onPrimaryContainer = Zinc100,

    // Colores secundarios - Slate neutro
    secondary = Slate400,
    onSecondary = Zinc900,
    secondaryContainer = Slate800,
    onSecondaryContainer = Zinc100,

    // Colores terciarios - Acento sutil
    tertiary = Slate500,
    onTertiary = Zinc900,
    tertiaryContainer = Slate700,
    onTertiaryContainer = Zinc100,

    // Colores de error
    error = Red500,
    onError = Zinc900,
    errorContainer = Red700,
    onErrorContainer = Red50,

    // Fondos y superficies
    background = Zinc950,
    onBackground = Zinc50,
    surface = Zinc950,
    onSurface = Zinc50,
    surfaceVariant = Zinc900,
    onSurfaceVariant = Zinc400,

    // Bordes y divisores
    outline = Zinc700,
    outlineVariant = Zinc800,

    // Otros
    scrim = BlackAlpha20,
    inverseSurface = Zinc50,
    inverseOnSurface = Zinc900,
    inversePrimary = Blue600,
    surfaceTint = Blue500
)

/**
 * Tema principal de la aplicación
 *
 * @param darkTheme Si debe usar el tema oscuro (por defecto sigue el sistema)
 * @param dynamicColor Si debe usar colores dinámicos en Android 12+ (deshabilitado por defecto para mantener consistencia)
 * @param content Contenido composable de la aplicación
 */
@Composable
fun EcommerceappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Deshabilitado para mantener diseño consistente
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