package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Componentes de botones reutilizables con estilo shadcn/ui
 *
 * Botones consistentes en toda la aplicación con variantes:
 * - Primary: Acción principal
 * - Secondary: Acción secundaria
 * - Outline: Alternativa sutil
 * - Destructive: Acciones de eliminación
 * - Ghost: Acción minimalista
 */

/**
 * Botón primario - Acción principal
 *
 * Uso: Acciones principales como "Iniciar Sesión", "Confirmar Compra"
 */
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(AppDimensions.buttonHeightNormal),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = AppDimensions.elevationNone,
            pressedElevation = AppDimensions.elevationXS
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(AppDimensions.iconSizeSmall)
                )
                Spacer(modifier = Modifier.width(AppDimensions.spaceS))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Botón secundario - Acción secundaria
 *
 * Uso: Acciones menos importantes como "Cancelar", "Ver más"
 */
@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(AppDimensions.buttonHeightNormal),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = AppDimensions.elevationNone
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppDimensions.iconSizeSmall)
            )
            Spacer(modifier = Modifier.width(AppDimensions.spaceS))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Botón outline - Alternativa sutil
 *
 * Uso: Acciones alternativas como "Ver carrito", "Guardar dirección"
 */
@Composable
fun OutlineButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(AppDimensions.buttonHeightNormal),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(
            width = AppDimensions.borderWidth,
            color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppDimensions.iconSizeSmall)
            )
            Spacer(modifier = Modifier.width(AppDimensions.spaceS))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Botón destructivo - Acciones de eliminación
 *
 * Uso: Acciones peligrosas como "Eliminar", "Cerrar sesión"
 */
@Composable
fun DestructiveButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(AppDimensions.buttonHeightNormal),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = AppDimensions.elevationNone
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppDimensions.iconSizeSmall)
            )
            Spacer(modifier = Modifier.width(AppDimensions.spaceS))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Botón ghost - Acción minimalista
 *
 * Uso: Acciones terciarias, enlaces de texto
 */
@Composable
fun GhostButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(AppDimensions.buttonHeightNormal),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppDimensions.iconSizeSmall)
            )
            Spacer(modifier = Modifier.width(AppDimensions.spaceS))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Botón pequeño - Versión compacta
 *
 * Uso: Espacios reducidos, botones en cards
 */
@Composable
fun SmallButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    when (variant) {
        ButtonVariant.Primary -> Button(
            onClick = onClick,
            modifier = modifier.height(AppDimensions.buttonHeightSmall),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(
                horizontal = AppDimensions.spaceNormal,
                vertical = AppDimensions.spaceS
            )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        }
        ButtonVariant.Outline -> OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(AppDimensions.buttonHeightSmall),
            enabled = enabled,
            border = BorderStroke(
                width = AppDimensions.borderWidth,
                color = MaterialTheme.colorScheme.outline
            ),
            contentPadding = PaddingValues(
                horizontal = AppDimensions.spaceNormal,
                vertical = AppDimensions.spaceS
            )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

/**
 * Variantes de botón para SmallButton
 */
enum class ButtonVariant {
    Primary,
    Outline
}
