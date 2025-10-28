package com.gymnastic.ecommerceapp.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Sistema de espaciado y dimensiones profesional
 *
 * Valores consistentes para espaciado, elevaciones y radios de borde.
 * Inspirado en el sistema de diseño shadcn/ui.
 */

object AppDimensions {

    // ========== ESPACIADO ==========

    /**
     * Espaciado extra pequeño (4dp)
     * Uso: Espacios muy pequeños, padding interno de badges
     */
    val spaceXS: Dp = 4.dp

    /**
     * Espaciado pequeño (8dp)
     * Uso: Espaciado entre elementos cercanos, padding de chips
     */
    val spaceS: Dp = 8.dp

    /**
     * Espaciado medio (12dp)
     * Uso: Espaciado estándar entre elementos relacionados
     */
    val spaceM: Dp = 12.dp

    /**
     * Espaciado normal (16dp)
     * Uso: Padding estándar de cards, espaciado entre secciones
     */
    val spaceNormal: Dp = 16.dp

    /**
     * Espaciado grande (24dp)
     * Uso: Separación entre secciones principales
     */
    val spaceL: Dp = 24.dp

    /**
     * Espaciado extra grande (32dp)
     * Uso: Separación de grupos de contenido, márgenes principales
     */
    val spaceXL: Dp = 32.dp

    /**
     * Espaciado doble extra grande (48dp)
     * Uso: Separaciones muy grandes, headers
     */
    val space2XL: Dp = 48.dp

    // ========== ELEVACIONES ==========

    /**
     * Sin elevación (0dp)
     * Uso: Elementos planos
     */
    val elevationNone: Dp = 0.dp

    /**
     * Elevación extra pequeña (1dp)
     * Uso: Cards sutiles, dividers elevados
     */
    val elevationXS: Dp = 1.dp

    /**
     * Elevación pequeña (2dp)
     * Uso: Cards estándar, botones
     */
    val elevationS: Dp = 2.dp

    /**
     * Elevación media (4dp)
     * Uso: Cards destacados, bottom sheets
     */
    val elevationM: Dp = 4.dp

    /**
     * Elevación grande (8dp)
     * Uso: Modales, floating action buttons
     */
    val elevationL: Dp = 8.dp

    /**
     * Elevación extra grande (12dp)
     * Uso: Diálogos importantes
     */
    val elevationXL: Dp = 12.dp

    // ========== RADIOS DE BORDE ==========

    /**
     * Radio pequeño (4dp)
     * Uso: Badges, chips pequeños
     */
    val radiusS: Dp = 4.dp

    /**
     * Radio medio (8dp)
     * Uso: Botones, text fields, cards pequeños
     */
    val radiusM: Dp = 8.dp

    /**
     * Radio normal (12dp)
     * Uso: Cards estándar, imágenes
     */
    val radiusNormal: Dp = 12.dp

    /**
     * Radio grande (16dp)
     * Uso: Cards grandes, bottom sheets
     */
    val radiusL: Dp = 16.dp

    /**
     * Radio extra grande (24dp)
     * Uso: Elementos muy redondeados, diálogos
     */
    val radiusXL: Dp = 24.dp

    /**
     * Radio completo (circular)
     * Uso: Avatares, botones circulares
     */
    val radiusFull: Dp = 9999.dp

    // ========== TAMAÑOS DE COMPONENTES ==========

    /**
     * Altura estándar de botones (48dp)
     */
    val buttonHeightNormal: Dp = 48.dp

    /**
     * Altura de botones pequeños (40dp)
     */
    val buttonHeightSmall: Dp = 40.dp

    /**
     * Altura de botones grandes (56dp)
     */
    val buttonHeightLarge: Dp = 56.dp

    /**
     * Altura de text fields (56dp)
     */
    val textFieldHeight: Dp = 56.dp

    /**
     * Tamaño de iconos pequeños (16dp)
     */
    val iconSizeSmall: Dp = 16.dp

    /**
     * Tamaño de iconos normales (24dp)
     */
    val iconSizeNormal: Dp = 24.dp

    /**
     * Tamaño de iconos grandes (32dp)
     */
    val iconSizeLarge: Dp = 32.dp

    /**
     * Tamaño de avatares pequeños (40dp)
     */
    val avatarSizeSmall: Dp = 40.dp

    /**
     * Tamaño de avatares normales (56dp)
     */
    val avatarSizeNormal: Dp = 56.dp

    /**
     * Tamaño de avatares grandes (80dp)
     */
    val avatarSizeLarge: Dp = 80.dp

    // ========== ANCHOS Y ALTURAS ==========

    /**
     * Altura mínima para áreas táctiles (48dp)
     * Según Material Design guidelines
     */
    val minTouchTarget: Dp = 48.dp

    /**
     * Ancho de dividers finos (1dp)
     */
    val dividerThickness: Dp = 1.dp

    /**
     * Ancho de bordes (1dp)
     */
    val borderWidth: Dp = 1.dp

    /**
     * Ancho de bordes gruesos (2dp)
     */
    val borderWidthThick: Dp = 2.dp
}
