package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

/**
 * Componentes de diálogos reutilizables con estilo shadcn/ui
 *
 * Diálogos consistentes para:
 * - Confirmaciones
 * - Alertas
 * - Información
 */

/**
 * Diálogo de confirmación - Para acciones que requieren confirmación
 *
 * Uso: "¿Estás seguro de cerrar sesión?", "¿Eliminar este item?"
 */
@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Confirmar",
    dismissText: String = "Cancelar",
    icon: ImageVector? = null,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            if (isDestructive) {
                DestructiveButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    text = confirmText
                )
            } else {
                PrimaryButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    text = confirmText
                )
            }
        },
        dismissButton = {
            GhostButton(
                onClick = onDismiss,
                text = dismissText
            )
        }
    )
}

/**
 * Diálogo de información - Para mostrar información importante
 *
 * Uso: Avisos, mensajes informativos
 */
@Composable
fun InfoDialog(
    onDismiss: () -> Unit,
    title: String,
    message: String,
    buttonText: String = "Entendido",
    icon: ImageVector? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            PrimaryButton(
                onClick = onDismiss,
                text = buttonText
            )
        }
    )
}

/**
 * Diálogo simple con contenido personalizado
 *
 * Uso: Cuando necesitas contenido completamente personalizado
 */
@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    title: String? = null,
    icon: ImageVector? = null,
    content: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = icon?.let { { Icon(imageVector = it, contentDescription = null) } },
        title = title?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = content,
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}
