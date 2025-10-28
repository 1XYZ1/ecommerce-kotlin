package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gymnastic.ecommerceapp.data.local.CartItem

/**
 * Tarjeta simple para mostrar un ítem del carrito
 */
@Composable
fun CartItemCard(
    cartItem: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cartItem.productImageUrl)
                    .build(),
                contentDescription = cartItem.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.productName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%.2f", cartItem.productPrice)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Controles de cantidad
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        if (cartItem.quantity > 1) {
                            onUpdateQuantity(cartItem.quantity - 1)
                        }
                    }) {
                        Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = cartItem.quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(onClick = {
                        onUpdateQuantity(cartItem.quantity + 1)
                    }) {
                        Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                IconButton(onClick = onRemoveItem) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
