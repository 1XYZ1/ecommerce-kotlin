package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gymnastic.ecommerceapp.data.local.Direccion
import com.gymnastic.ecommerceapp.ui.viewmodels.DireccionViewModel

/**
 * Pantalla para gestionar direcciones guardadas del usuario
 *
 * Permite al usuario:
 * - Ver todas sus direcciones guardadas
 * - Agregar nuevas direcciones
 * - Editar direcciones existentes
 * - Eliminar direcciones
 * - Establecer dirección predeterminada
 *
 * Diseño simple y funcional con Material 3.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionesGuardadasScreen(
    onBack: () -> Unit,
    direccionViewModel: DireccionViewModel = hiltViewModel()
) {
    // Estados del ViewModel
    val direcciones by direccionViewModel.direcciones.collectAsState()
    val estaCargando by direccionViewModel.estaCargando.collectAsState()
    val mensajeError by direccionViewModel.mensajeError.collectAsState()
    val mensajeExito by direccionViewModel.mensajeExito.collectAsState()

    // Estados locales para diálogos
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var direccionSeleccionada by remember { mutableStateOf<Direccion?>(null) }

    // Mostrar mensajes de éxito/error
    LaunchedEffect(mensajeExito) {
        if (mensajeExito != null) {
            direccionViewModel.limpiarMensajeExito()
        }
    }

    LaunchedEffect(mensajeError) {
        if (mensajeError != null) {
            direccionViewModel.limpiarError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Direcciones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoAgregar = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar dirección",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mostrar mensaje de éxito
            if (mensajeExito != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = mensajeExito!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Mostrar mensaje de error
            if (mensajeError != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = mensajeError!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Lista de direcciones
            if (estaCargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (direcciones.isEmpty()) {
                // Estado vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Sin direcciones",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "No tienes direcciones guardadas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Toca el botón + para agregar tu primera dirección",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(direcciones) { direccion ->
                        DireccionCard(
                            direccion = direccion,
                            onEditar = {
                                direccionSeleccionada = direccion
                                mostrarDialogoEditar = true
                            },
                            onEliminar = {
                                direccionSeleccionada = direccion
                                mostrarDialogoEliminar = true
                            },
                            onEstablecerPredeterminada = {
                                direccionViewModel.establecerDireccionPredeterminada(direccion.id)
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo para agregar nueva dirección
    if (mostrarDialogoAgregar) {
        DialogoDireccion(
            titulo = "Agregar Nueva Dirección",
            onGuardar = { nombre, telefono, direccion, esPredeterminada ->
                direccionViewModel.guardarDireccion(nombre, telefono, direccion, esPredeterminada)
                mostrarDialogoAgregar = false
            },
            onCancelar = { mostrarDialogoAgregar = false }
        )
    }

    // Diálogo para editar dirección
    if (mostrarDialogoEditar && direccionSeleccionada != null) {
        DialogoDireccion(
            titulo = "Editar Dirección",
            direccionInicial = direccionSeleccionada,
            onGuardar = { nombre, telefono, direccion, esPredeterminada ->
                direccionViewModel.actualizarDireccion(
                    direccionSeleccionada!!.id,
                    nombre,
                    telefono,
                    direccion,
                    esPredeterminada
                )
                mostrarDialogoEditar = false
                direccionSeleccionada = null
            },
            onCancelar = {
                mostrarDialogoEditar = false
                direccionSeleccionada = null
            }
        )
    }

    // Diálogo de confirmación para eliminar
    if (mostrarDialogoEliminar && direccionSeleccionada != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
                direccionSeleccionada = null
            },
            title = { Text("Eliminar Dirección") },
            text = {
                Text("¿Estás seguro de que quieres eliminar esta dirección? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        direccionViewModel.eliminarDireccion(direccionSeleccionada!!.id)
                        mostrarDialogoEliminar = false
                        direccionSeleccionada = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        direccionSeleccionada = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Card para mostrar una dirección individual
 */
@Composable
private fun DireccionCard(
    direccion: Direccion,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onEstablecerPredeterminada: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header con nombre y badge de predeterminada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = direccion.nombreCompleto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (direccion.esPredeterminada) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Predeterminada",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // Información de contacto
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Teléfono",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = direccion.telefono,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Dirección completa
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Dirección",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = direccion.direccionCompleta,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón editar
                OutlinedButton(
                    onClick = onEditar,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar")
                }

                // Botón eliminar
                OutlinedButton(
                    onClick = onEliminar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar")
                }

                // Botón establecer como predeterminada (solo si no es predeterminada)
                if (!direccion.esPredeterminada) {
                    OutlinedButton(
                        onClick = onEstablecerPredeterminada,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Predeterminada",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Predeterminada")
                    }
                }
            }
        }
    }
}

/**
 * Diálogo para agregar o editar una dirección
 */
@Composable
private fun DialogoDireccion(
    titulo: String,
    direccionInicial: Direccion? = null,
    onGuardar: (String, String, String, Boolean) -> Unit,
    onCancelar: () -> Unit
) {
    var nombreCompleto by remember { mutableStateOf(direccionInicial?.nombreCompleto ?: "") }
    var telefono by remember { mutableStateOf(direccionInicial?.telefono ?: "") }
    var direccionCompleta by remember { mutableStateOf(direccionInicial?.direccionCompleta ?: "") }
    var esPredeterminada by remember { mutableStateOf(direccionInicial?.esPredeterminada ?: false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(titulo) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = nombreCompleto,
                    onValueChange = { nombreCompleto = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccionCompleta,
                    onValueChange = { direccionCompleta = it },
                    label = { Text("Dirección completa") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = esPredeterminada,
                        onCheckedChange = { esPredeterminada = it }
                    )
                    Text("Establecer como dirección predeterminada")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombreCompleto.isNotBlank() && telefono.isNotBlank() && direccionCompleta.isNotBlank()) {
                        onGuardar(nombreCompleto, telefono, direccionCompleta, esPredeterminada)
                    }
                },
                enabled = nombreCompleto.isNotBlank() && telefono.isNotBlank() && direccionCompleta.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}
