package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.gymnastic.ecommerceapp.domain.Product
import com.gymnastic.ecommerceapp.ui.components.ProductCard
import com.gymnastic.ecommerceapp.ui.components.SearchTextField
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import com.gymnastic.ecommerceapp.utils.NativeUtils

/**
 * Pantalla principal simplificada con búsqueda integrada
 *
 * Esta pantalla muestra todos los productos disponibles y permite buscar
 * productos por nombre usando un campo de búsqueda simple en el TopAppBar.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - remember: Mantiene el estado durante recomposiciones
 * - derivedStateOf: Calcula valores derivados de forma eficiente
 * - filter: Filtra listas basándose en condiciones
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cartViewModel: CartViewModel,
    onProductClick: (Product) -> Unit
) {
    val contexto = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ========== ESTADOS LOCALES ==========

    /**
     * Texto de búsqueda ingresado por el usuario
     *
     * remember mantiene este valor durante las recomposiciones de la UI.
     * Solo se reinicia cuando el Composable se destruye completamente.
     */
    var textoBusqueda by remember { mutableStateOf("") }

    // ========== DATOS ==========

    /**
     * Lista completa de productos disponibles
     */
    val todosLosProductos = cartViewModel.obtenerTodosLosProductos()

    /**
     * Lista filtrada de productos basada en la búsqueda
     *
     * derivedStateOf calcula este valor solo cuando cambia textoBusqueda.
     * Es más eficiente que usar remember con un key.
     */
    val productosFiltrados by remember {
        derivedStateOf {
            if (textoBusqueda.isBlank()) {
                // Si no hay texto de búsqueda, mostrar todos los productos
                todosLosProductos
            } else {
                // Filtrar productos que contengan el texto de búsqueda en el nombre o descripción
                todosLosProductos.filter { producto ->
                    producto.name.contains(textoBusqueda, ignoreCase = true) ||
                    producto.description.contains(textoBusqueda, ignoreCase = true)
                }
            }
        }
    }

    // ========== UI ==========

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Tienda",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ========== BARRA DE BÚSQUEDA ==========

            /**
             * Campo de búsqueda con diseño shadcn/ui y botón para limpiar
             *
             * Se actualiza en tiempo real mientras el usuario escribe.
             * Incluye botón X para limpiar la búsqueda rápidamente.
             */
            SearchTextField(
                value = textoBusqueda,
                onValueChange = { nuevoTexto ->
                    textoBusqueda = nuevoTexto
                },
                placeholder = "Buscar productos...",
                leadingIcon = Icons.Default.Search,
                trailingIcon = if (textoBusqueda.isNotEmpty()) {
                    {
                        IconButton(onClick = { textoBusqueda = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimensions.spaceNormal, vertical = AppDimensions.spaceS)
            )

            // ========== TÍTULO DE SECCIÓN ==========

            Text(
                text = if (textoBusqueda.isBlank()) {
                    "Productos Destacados"
                } else {
                    "Resultados de búsqueda (${productosFiltrados.size})"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = AppDimensions.spaceNormal, vertical = AppDimensions.spaceS)
            )

            // ========== GRID DE PRODUCTOS ==========

            if (productosFiltrados.isEmpty() && textoBusqueda.isNotBlank()) {
                // Mostrar mensaje cuando no hay resultados
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(AppDimensions.space2XL),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(AppDimensions.spaceM)
                    ) {
                        Text(
                            text = "🔍",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = "Sin resultados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "No se encontraron productos para \"$textoBusqueda\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Mostrar grid de productos
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productosFiltrados) { producto ->
                        ProductCard(
                            product = producto,
                            onProductClick = onProductClick,
                            onAddToCart = {
                                cartViewModel.agregarAlCarrito(producto)
                                NativeUtils.vibrateOnAddToCart(contexto)
                                // Mostrar Snackbar con feedback
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${producto.name} agregado al carrito",
                                        actionLabel = "Ver carrito",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        // Navegar al carrito si presiona "Ver carrito"
                                        // Necesitarías pasar un callback onNavigateToCart
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
