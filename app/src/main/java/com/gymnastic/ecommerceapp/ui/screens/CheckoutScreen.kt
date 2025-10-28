package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gymnastic.ecommerceapp.data.local.Direccion
import com.gymnastic.ecommerceapp.ui.viewmodels.CartViewModel
import com.gymnastic.ecommerceapp.ui.viewmodels.DireccionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    direccionViewModel: DireccionViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val total = cartItems.sumOf { it.productPrice * it.quantity }

    // Estados del ViewModel de direcciones
    val direcciones by direccionViewModel.direcciones.collectAsState()
    val direccionPredeterminada by direccionViewModel.direccionPredeterminada.collectAsState()

    // Estados del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }

    // Estados de validación
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var paymentError by remember { mutableStateOf("") }

    // Estados para diálogos
    var mostrarSelectorDirecciones by remember { mutableStateOf(false) }
    var mostrarDialogoGuardarDireccion by remember { mutableStateOf(false) }

    val paymentMethods = listOf("Tarjeta de Crédito", "Efectivo", "Transferencia Bancaria")
    var expanded by remember { mutableStateOf(false) }

    // Autocompletar datos del usuario al cargar la pantalla
    LaunchedEffect(Unit) {
        // Aquí se podría obtener el email del usuario logueado
        // Por ahora lo dejamos vacío para que el usuario lo ingrese
    }

    // Autocompletar con dirección predeterminada si existe
    LaunchedEffect(direccionPredeterminada) {
        direccionPredeterminada?.let { direccion ->
            name = direccion.nombreCompleto
            phone = direccion.telefono
            address = direccion.direccionCompleta
        }
    }

    // Función para validar el formulario
    fun validateForm(): Boolean {
        var isValid = true

        nameError = when {
            name.isBlank() -> "El nombre es requerido"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> ""
        }
        if (nameError.isNotEmpty()) isValid = false

        emailError = when {
            email.isBlank() -> "El email es requerido"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato de email inválido"
            else -> ""
        }
        if (emailError.isNotEmpty()) isValid = false

        phoneError = when {
            phone.isBlank() -> "El teléfono es requerido"
            phone.length != 10 -> "El teléfono debe tener 10 dígitos"
            !phone.all { it.isDigit() } -> "Solo se permiten números"
            else -> ""
        }
        if (phoneError.isNotEmpty()) isValid = false

        addressError = when {
            address.isBlank() -> "La dirección es requerida"
            address.length < 10 -> "La dirección debe tener al menos 10 caracteres"
            else -> ""
        }
        if (addressError.isNotEmpty()) isValid = false

        paymentError = if (paymentMethod.isBlank()) {
            isValid = false
            "Selecciona un método de pago"
        } else ""

        return isValid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Resumen de la compra
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen de Compra", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.productName} x${item.quantity}", fontSize = 14.sp)
                            Text("$${String.format("%.2f", item.productPrice * item.quantity)}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("$${String.format("%.2f", total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Formulario
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Información de Contacto", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        // Botón para usar dirección guardada
                        if (direcciones.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { mostrarSelectorDirecciones = true },
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = "Usar dirección guardada",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Usar dirección guardada", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; nameError = "" },
                        label = { Text("Nombre completo") },
                        isError = nameError.isNotEmpty(),
                        supportingText = if (nameError.isNotEmpty()) { { Text(nameError, color = MaterialTheme.colorScheme.error) } } else null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = "" },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError.isNotEmpty(),
                        supportingText = if (emailError.isNotEmpty()) { { Text(emailError, color = MaterialTheme.colorScheme.error) } } else null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo teléfono
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; phoneError = "" },
                        label = { Text("Teléfono") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError.isNotEmpty(),
                        supportingText = if (phoneError.isNotEmpty()) { { Text(phoneError, color = MaterialTheme.colorScheme.error) } } else null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo dirección
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it; addressError = "" },
                        label = { Text("Dirección completa") },
                        minLines = 2,
                        isError = addressError.isNotEmpty(),
                        supportingText = if (addressError.isNotEmpty()) { { Text(addressError, color = MaterialTheme.colorScheme.error) } } else null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo método de pago
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = paymentMethod,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Método de pago") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            isError = paymentError.isNotEmpty(),
                            supportingText = if (paymentError.isNotEmpty()) { { Text(paymentError, color = MaterialTheme.colorScheme.error) } } else null,
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            paymentMethods.forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(method) },
                                    onClick = {
                                        paymentMethod = method
                                        paymentError = ""
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón para guardar esta dirección
                    OutlinedButton(
                        onClick = { mostrarDialogoGuardarDireccion = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Guardar dirección",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar esta dirección para futuras compras")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón confirmar compra
            Button(
                onClick = {
                    if (validateForm()) {
                        cartViewModel.clearCart()
                        onSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp)
            ) {
                Text("Confirmar Compra", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Selector de direcciones guardadas
    if (mostrarSelectorDirecciones) {
        SelectorDireccionesDialog(
            direcciones = direcciones,
            onSeleccionarDireccion = { direccion ->
                name = direccion.nombreCompleto
                phone = direccion.telefono
                address = direccion.direccionCompleta
                mostrarSelectorDirecciones = false
            },
            onCancelar = { mostrarSelectorDirecciones = false }
        )
    }

    // Diálogo para guardar dirección actual
    if (mostrarDialogoGuardarDireccion) {
        DialogoGuardarDireccion(
            nombreInicial = name,
            telefonoInicial = phone,
            direccionInicial = address,
            onGuardar = { nombre, telefono, direccion, esPredeterminada ->
                direccionViewModel.guardarDireccion(nombre, telefono, direccion, esPredeterminada)
                mostrarDialogoGuardarDireccion = false
            },
            onCancelar = { mostrarDialogoGuardarDireccion = false }
        )
    }
}

/**
 * Diálogo para seleccionar una dirección guardada
 */
@Composable
private fun SelectorDireccionesDialog(
    direcciones: List<Direccion>,
    onSeleccionarDireccion: (Direccion) -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Seleccionar Dirección") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                direcciones.forEach { direccion ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSeleccionarDireccion(direccion) }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = direccion.nombreCompleto,
                                    fontWeight = FontWeight.Bold
                                )
                                if (direccion.esPredeterminada) {
                                    Text(
                                        text = "Predeterminada",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Text(
                                text = direccion.telefono,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = direccion.direccionCompleta,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Diálogo para guardar la dirección actual
 */
@Composable
private fun DialogoGuardarDireccion(
    nombreInicial: String,
    telefonoInicial: String,
    direccionInicial: String,
    onGuardar: (String, String, String, Boolean) -> Unit,
    onCancelar: () -> Unit
) {
    var nombreCompleto by remember { mutableStateOf(nombreInicial) }
    var telefono by remember { mutableStateOf(telefonoInicial) }
    var direccionCompleta by remember { mutableStateOf(direccionInicial) }
    var esPredeterminada by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Guardar Dirección") },
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