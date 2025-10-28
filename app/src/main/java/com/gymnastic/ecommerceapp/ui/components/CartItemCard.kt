package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Tarjeta de item del carrito con diseño profesional shadcn/ui
 *
 * Muestra producto en el carrito con controles de cantidad y eliminación.
 */
@Composable
fun CartItemCard(
    cartItem: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    OutlinedAppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.spaceM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cartItem.productImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = cartItem.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Información del producto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.spaceXS)
            ) {
                Text(
                    text = cartItem.productName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$${String.format("%.2f", cartItem.productPrice)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Controles de cantidad
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppDimensions.spaceXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón decrementar
                    IconButton(
                        onClick = {
                            if (cartItem.quantity > 1) {
                                onUpdateQuantity(cartItem.quantity - 1)
                            }
                        },
                        modifier = Modifier.size(32.dp),
                        enabled = cartItem.quantity > 1,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(
                            text = "−",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Cantidad
                    Text(
                        text = cartItem.quantity.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.widthIn(min = 24.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Botón incrementar
                    IconButton(
                        onClick = {
                            onUpdateQuantity(cartItem.quantity + 1)
                        },
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar cantidad",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Botón eliminar
            IconButton(
                onClick = onRemoveItem,
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar del carrito",
                    modifier = Modifier.size(AppDimensions.iconSizeNormal)
                )
            }
        }
    }
}
