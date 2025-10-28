package com.gymnastic.ecommerceapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.gymnastic.ecommerceapp.domain.Product
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions

/**
 * Tarjeta de producto con diseño profesional shadcn/ui
 *
 * Diseño limpio y elegante para mostrar productos en el grid.
 * Incluye imagen, nombre, precio y botón de agregar al carrito.
 */
@Composable
fun ProductCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    onAddToCart: () -> Unit
) {
    ElevatedAppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product) }
    ) {
        Column(
            modifier = Modifier.padding(0.dp)
        ) {
            // Imagen del producto
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(AppDimensions.spaceM))

            // Nombre del producto
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(AppDimensions.spaceXS))

            // Precio y botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Precio destacado
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Botón pequeño para agregar
                IconButton(
                    onClick = onAddToCart,
                    modifier = Modifier.size(AppDimensions.iconSizeLarge),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar al carrito",
                        modifier = Modifier.size(AppDimensions.iconSizeSmall)
                    )
                }
            }
        }
    }
}