package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gymnastic.ecommerceapp.ui.components.AppOutlinedTextField
import com.gymnastic.ecommerceapp.ui.components.PrimaryButton
import com.gymnastic.ecommerceapp.ui.viewmodels.AdminViewModel
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla SIMPLIFICADA para editar productos
 *
 * Solo permite editar:
 * - Título
 * - Descripción
 * - Precio
 * - Stock
 *
 * Las imágenes NO son editables
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductFormScreen(
    navController: NavController,
    productId: String? = null,
    cartViewModel: CartViewModel = hiltViewModel(),
    adminViewModel: AdminViewModel = hiltViewModel()
) {
    val esEdicion = productId != null

    // Estados del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var estaCargandoProducto by remember { mutableStateOf(false) }

    val mensajeError by adminViewModel.mensajeError.collectAsState()
    val operacionExitosa by adminViewModel.operacionExitosa.collectAsState()
    val estaCargando by adminViewModel.estaCargando.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Cargar datos del producto si es edición
    LaunchedEffect(productId) {
        if (productId != null) {
            estaCargandoProducto = true
            val producto = cartViewModel.obtenerProductoPorId(productId)
            producto?.let {
                titulo = it.name
                descripcion = it.description
                precio = it.price.toString()
                stock = it.stock.toString()
            }
            estaCargandoProducto = false
        }
    }

    // Mostrar mensajes de error
    LaunchedEffect(mensajeError) {
        mensajeError?.let {
            snackbarHostState.showSnackbar(it)
            adminViewModel.limpiarMensajes()
        }
    }

    // Navegar atrás cuando la operación es exitosa
    LaunchedEffect(operacionExitosa) {
        if (operacionExitosa) {
            snackbarHostState.showSnackbar(
                if (esEdicion) "Producto actualizado"
                else "Producto creado"
            )
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar Producto" else "Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (estaCargandoProducto) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                AppOutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = "Título del producto",
                    modifier = Modifier.fillMaxWidth()
                )

                // Descripción
                AppOutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = "Descripción",
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                // Precio
                AppOutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = "Precio",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                // Stock
                AppOutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = "Stock disponible",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de guardar
                PrimaryButton(
                    onClick = {
                        val precioDouble = precio.toDoubleOrNull()
                        val stockInt = stock.toIntOrNull()

                        if (esEdicion && productId != null) {
                            adminViewModel.actualizarProducto(
                                id = productId,
                                nombre = titulo,
                                descripcion = descripcion,
                                precio = precio,
                                stock = stock
                            )
                        } else {
                            adminViewModel.crearProducto(
                                nombre = titulo,
                                descripcion = descripcion,
                                precio = precio,
                                stock = stock
                            )
                        }
                    },
                    text = if (esEdicion) "Guardar Cambios" else "Crear Producto",
                    enabled = !estaCargando,
                    modifier = Modifier.fillMaxWidth()
                )

                if (estaCargando) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
