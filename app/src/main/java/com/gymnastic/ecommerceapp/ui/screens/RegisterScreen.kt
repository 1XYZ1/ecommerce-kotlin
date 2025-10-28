package com.gymnastic.ecommerceapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
 * Pantalla de registro con diseño profesional shadcn/ui
 *
 * Permite a nuevos usuarios crear una cuenta.
 */
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val estaCargando by authViewModel.estaCargando.collectAsState()
    val mensajeError by authViewModel.mensajeError.collectAsState()
    val estaLogueado by authViewModel.estaLogueado.collectAsState()

    LaunchedEffect(estaLogueado) {
        if (estaLogueado) {
            onRegisterSuccess()
        }
    }

    LaunchedEffect(name, email, password, confirmPassword) {
        if (mensajeError != null) {
            authViewModel.limpiarError()
        }
    }

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
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Únete a nuestra tienda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppDimensions.spaceS))

                AppOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre completo",
                    leadingIcon = Icons.Default.Person,
                    enabled = !estaCargando,
                    modifier = Modifier.fillMaxWidth()
                )

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
                    error = if (password.isNotEmpty() && password.length < 6) "Mínimo 6 caracteres" else null,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                AppOutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirmar contraseña",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(
                                text = if (confirmPasswordVisible) "Ocultar" else "Mostrar",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    enabled = !estaCargando,
                    error = if (confirmPassword.isNotEmpty() && password != confirmPassword) "Las contraseñas no coinciden" else null,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

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

                PrimaryButton(
                    onClick = {
                        authViewModel.registrarUsuario(name, email, password, confirmPassword)
                    },
                    text = "Crear Cuenta",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estaCargando &&
                            name.isNotBlank() &&
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            confirmPassword.isNotBlank() &&
                            password.length >= 6 &&
                            password == confirmPassword,
                    isLoading = estaCargando
                )

                Spacer(modifier = Modifier.height(AppDimensions.spaceM))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes cuenta? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    GhostButton(
                        onClick = onNavigateToLogin,
                        text = "Inicia sesión aquí"
                    )
                }
            }
        }
    }
}
