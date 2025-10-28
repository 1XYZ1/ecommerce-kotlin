package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Componentes de cards reutilizables con estilo shadcn/ui
 *
 * Cards consistentes para agrupar contenido:
 * - AppCard: Card básico con padding estándar
 * - ElevatedCard: Card con elevación sutil
 * - OutlinedCard: Card con borde
 */

/**
 * Card básico - Superficie simple
 *
 * Uso: Contenedores principales de información
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = AppDimensions.elevationNone,
                pressedElevation = AppDimensions.elevationXS
            ),
            border = BorderStroke(
                width = AppDimensions.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.spaceNormal),
                content = content
            )
        }
    } else {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = AppDimensions.elevationNone
            ),
            border = BorderStroke(
                width = AppDimensions.borderWidth,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.spaceNormal),
                content = content
            )
        }
    }
}

/**
 * Card elevado - Con sombra sutil
 *
 * Uso: Cards que necesitan destacar del fondo
 */
@Composable
fun ElevatedAppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        ElevatedCard(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = AppDimensions.elevationS,
                pressedElevation = AppDimensions.elevationM
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.spaceNormal),
                content = content
            )
        }
    } else {
        ElevatedCard(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = AppDimensions.elevationS
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.spaceNormal),
                content = content
            )
        }
    }
}

/**
 * Card outlined - Con borde destacado
 *
 * Uso: Cards que necesitan separación visual clara
 */
@Composable
fun OutlinedAppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    borderColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (onClick != null) {
        OutlinedCard(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = BorderStroke(
                width = AppDimensions.borderWidth,
                color = borderColor ?: MaterialTheme.colorScheme.outline
            ),
            elevation = CardDefaults.outlinedCardElevation(
                defaultElevation = AppDimensions.elevationNone,
                pressedElevation = AppDimensions.elevationXS
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.spaceNormal),
                content = content
            )
        }
    } else {
        OutlinedCard(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                width = AppDimensions.borderWidth,
                color = borderColor ?: MaterialTheme.colorScheme.outline
            ),
            elevation = CardDefaults.outlinedCardElevation(
                defaultElevation = AppDimensions.elevationNone
            )
        ) {
            Column(
                modifier = Modifier.padding(AppDimensions.spaceNormal),
                content = content
            )
        }
    }
}

/**
 * Card de información - Con color de fondo personalizado
 *
 * Uso: Alertas, mensajes informativos
 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppDimensions.elevationNone
        )
    ) {
        Column(
            modifier = Modifier.padding(AppDimensions.spaceNormal),
            content = content
        )
    }
}
