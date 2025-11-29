package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gymnastic.ecommerceapp.domain.Product
import com.gymnastic.ecommerceapp.ui.components.DestructiveButton
import com.gymnastic.ecommerceapp.ui.components.ElevatedAppCard
import com.gymnastic.ecommerceapp.ui.components.OutlineButton
import com.gymnastic.ecommerceapp.ui.components.ConfirmDialog
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions
import com.gymnastic.ecommerceapp.ui.viewmodels.AdminViewModel
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel

/**
 * Pantalla de lista de productos para administradores
 *
 * Muestra todos los productos con opciones para:
 * - Crear nuevo producto (FAB)
 * - Editar producto existente
 * - Eliminar producto
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductListScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(),
    adminViewModel: AdminViewModel = hiltViewModel()
) {
    var productos by remember { mutableStateOf<List<Product>>(emptyList()) }
    var estaCargando by remember { mutableStateOf(true) }
    var mostrarDialogoEliminar by remember { mutableStateOf<Product?>(null) }

    val operacionExitosa by adminViewModel.operacionExitosa.collectAsState()
    val mensajeError by adminViewModel.mensajeError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar productos al iniciar
    LaunchedEffect(Unit) {
        estaCargando = true
        productos = cartViewModel.obtenerTodosLosProductos()
        estaCargando = false
    }

    // Recargar productos cuando se complete una operación
    LaunchedEffect(operacionExitosa) {
        if (operacionExitosa) {
            productos = cartViewModel.obtenerTodosLosProductos()
            adminViewModel.limpiarMensajes()
        }
    }

    // Mostrar mensajes de error
    LaunchedEffect(mensajeError) {
        mensajeError?.let {
            snackbarHostState.showSnackbar(it)
            adminViewModel.limpiarMensajes()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("admin_product_create") }
            ) {
                Icon(Icons.Default.Add, "Nuevo Producto")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (estaCargando) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(productos) { producto ->
                    AdminProductItem(
                        producto = producto,
                        onEditClick = {
                            navController.navigate("admin_product_edit/${producto.id}")
                        },
                        onDeleteClick = {
                            mostrarDialogoEliminar = producto
                        }
                    )
                    Spacer(modifier = Modifier.height(AppDimensions.spaceS))
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    mostrarDialogoEliminar?.let { producto ->
        ConfirmDialog(
            onDismiss = { mostrarDialogoEliminar = null },
            onConfirm = {
                adminViewModel.eliminarProducto(producto.id)
                mostrarDialogoEliminar = null
            },
            title = "Eliminar Producto",
            message = "¿Estás seguro de que quieres eliminar '${producto.name}'?"
        )
    }
}

/**
 * Item individual de producto en la lista de admin
 */
@Composable
fun AdminProductItem(
    producto: Product,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedAppCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            AsyncImage(
                model = producto.imageUrl,
                contentDescription = producto.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Info del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Precio: $${producto.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botones de acción
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlineButton(
                    text = "Editar",
                    onClick = onEditClick,
                    modifier = Modifier.width(100.dp)
                )
                DestructiveButton(
                    text = "Eliminar",
                    onClick = onDeleteClick,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}
