package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Componentes para estados vacíos
 *
 * Pantallas cuando no hay contenido para mostrar.
 */

/**
 * Estado vacío completo - Para pantallas sin contenido
 *
 * Uso: Carrito vacío, sin resultados de búsqueda, sin direcciones
 */
@Composable
fun EmptyState(
    emoji: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimensions.space2XL),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppDimensions.spaceNormal)
        ) {
            // Emoji ilustrativo
            Text(
                text = emoji,
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(AppDimensions.spaceS))

            // Título
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            // Descripción
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Acción opcional
            if (action != null) {
                Spacer(modifier = Modifier.height(AppDimensions.spaceM))
                action()
            }
        }
    }
}

/**
 * Estado vacío compacto - Para secciones pequeñas
 *
 * Uso: Secciones dentro de una pantalla
 */
@Composable
fun CompactEmptyState(
    emoji: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppDimensions.spaceL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppDimensions.spaceS)
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Sin resultados de búsqueda - Estado específico
 *
 * Uso: Cuando una búsqueda no arroja resultados
 */
@Composable
fun NoSearchResults(
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    EmptyState(
        emoji = "🔍",
        title = "Sin resultados",
        description = "No encontramos productos para \"$searchQuery\".\nIntenta con otros términos de búsqueda.",
        modifier = modifier
    )
}

/**
 * Carrito vacío - Estado específico
 *
 * Uso: Cuando el carrito no tiene items
 */
@Composable
fun EmptyCart(
    modifier: Modifier = Modifier,
    onStartShopping: (() -> Unit)? = null
) {
    EmptyState(
        emoji = "🛒",
        title = "Tu carrito está vacío",
        description = "Agrega algunos productos para comenzar tu compra.",
        modifier = modifier,
        action = if (onStartShopping != null) {
            { PrimaryButton(onClick = onStartShopping, text = "Ir a comprar") }
        } else null
    )
}
