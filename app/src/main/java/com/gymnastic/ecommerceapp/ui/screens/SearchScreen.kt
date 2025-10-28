package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gymnastic.ecommerceapp.domain.Product
import com.gymnastic.ecommerceapp.ui.components.ProductCard
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import com.gymnastic.ecommerceapp.utils.NativeUtils
import androidx.compose.ui.platform.LocalContext

/**
 * Pantalla de búsqueda de productos
 *
 * Esta pantalla permite a los usuarios buscar productos por nombre o descripción.
 * Incluye:
 * - Barra de búsqueda en la parte superior
 * - Grid de productos filtrados por la búsqueda
 * - Funcionalidad de agregar al carrito
 * - Navegación a detalles del producto
 *
 * Utiliza un filtro en tiempo real que busca en el nombre y descripción
 * de los productos disponibles en el catálogo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    cartViewModel: CartViewModel,
    onProductClick: (Product) -> Unit
) {
    val context = LocalContext.current

    // Estado para el texto de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Obtener todos los productos del catálogo
    val allProducts = cartViewModel.getAllProducts()

    // Filtrar productos basado en la búsqueda
    val filteredProducts = remember(searchQuery, allProducts) {
        if (searchQuery.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar productos...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Ingresa el nombre del producto") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar resultados de búsqueda
        if (searchQuery.isBlank()) {
            // Estado inicial - mostrar mensaje de bienvenida
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Busca productos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Escribe el nombre del producto que buscas",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else if (filteredProducts.isEmpty()) {
            // No se encontraron resultados
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Sin resultados",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "No se encontraron productos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Intenta con otro término de búsqueda",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            // Mostrar productos encontrados
            Text(
                text = "Resultados (${filteredProducts.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = onProductClick,
                        onAddToCart = {
                            cartViewModel.addToCart(product)
                            NativeUtils.vibrateOnAddToCart(context)
                        }
                    )
                }
            }
        }
    }
}
