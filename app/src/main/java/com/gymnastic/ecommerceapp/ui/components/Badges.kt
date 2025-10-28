package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Componentes de badges y etiquetas con estilo shadcn/ui
 *
 * Badges para indicadores y etiquetas de estado.
 */

/**
 * Badge de contador - Para mostrar cantidad de items
 *
 * Uso: Contador del carrito, notificaciones
 */
@Composable
fun CountBadge(
    count: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.error,
    contentColor: Color = MaterialTheme.colorScheme.onError
) {
    if (count > 0) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (count > 99) "99+" else count.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Badge de estado - Para indicar estados o categorías
 *
 * Uso: "Predeterminado", "Nuevo", "Destacado"
 */
@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.Default
) {
    val (backgroundColor, contentColor) = when (variant) {
        BadgeVariant.Default -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        BadgeVariant.Success -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        BadgeVariant.Warning -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        BadgeVariant.Error -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        BadgeVariant.Info -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .padding(horizontal = AppDimensions.spaceS, vertical = AppDimensions.spaceXS),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Variantes de badge de estado
 */
enum class BadgeVariant {
    Default,    // Gris
    Success,    // Verde/Azul
    Warning,    // Amarillo
    Error,      // Rojo
    Info        // Neutro
}
