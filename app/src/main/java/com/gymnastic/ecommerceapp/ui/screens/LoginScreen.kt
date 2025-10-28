package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.gymnastic.ecommerceapp.ui.components.*
import com.gymnastic.ecommerceapp.ui.theme.AppDimensions
import com.gymnastic.ecommerceapp.ui.viewmodels.AuthViewModel

/**
 * Pantalla de inicio de sesión con diseño profesional shadcn/ui
 *
 * Permite a usuarios existentes autenticarse con email y contraseña.
 * Incluye validación en tiempo real y manejo de estados de carga y error.
 */
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Estados locales
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados del ViewModel
    val estaCargando by authViewModel.estaCargando.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()
    val estaLogueado by authViewModel.estaLogueado.collectAsState()

    // Navegar cuando el login sea exitoso
    LaunchedEffect(estaLogueado) {
        if (estaLogueado) {
            onLoginSuccess()
        }
    }

    // Limpiar errores cuando cambien los campos
    LaunchedEffect(email, password) {
        if (mensajeError != null) {
            authViewModel.limpiarError()
        }
    }

    // Layout principal centrado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimensions.spaceNormal),
        contentAlignment = Alignment.Center
    ) {
        ElevatedAppCard(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppDimensions.spaceNormal)
            ) {
                // Título
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Bienvenido de vuelta",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppDimensions.spaceS))

                // Campo de email
                AppOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "tu@email.com",
                    leadingIcon = Icons.Default.Email,
                    enabled = !estaCargando,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de contraseña
                AppOutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible) "Ocultar" else "Mostrar",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !estaCargando,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Mensaje de error
                mensajeError?.let { error ->
                    InfoCard(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppDimensions.spaceS))

                // Botón de login
                PrimaryButton(
                    onClick = {
                        authViewModel.iniciarSesion(email, password)
                    },
                    text = "Iniciar Sesión",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estaCargando && email.isNotBlank() && password.isNotBlank(),
                    isLoading = estaCargando
                )

                Spacer(modifier = Modifier.height(AppDimensions.spaceM))

                // Enlace para ir a registro
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    GhostButton(
                        onClick = onNavigateToRegister,
                        text = "Regístrate aquí"
                    )
                }
            }
        }
    }
}
