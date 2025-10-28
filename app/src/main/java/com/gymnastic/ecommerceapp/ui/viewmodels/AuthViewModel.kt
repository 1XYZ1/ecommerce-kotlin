package com.gymnastic.ecommerceapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymnastic.ecommerceapp.data.local.UserInfo
import com.gymnastic.ecommerceapp.data.local.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel simplificado para manejar la autenticación del usuario
 *
 * Versión simplificada con código más legible y variables en español.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferenciasUsuario: UserPreferences
) : ViewModel() {

    // Estados principales con variables en español
    private val _estaLogueado = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _estaLogueado.asStateFlow()

    private val _infoUsuario = MutableStateFlow(UserInfo("", "", false))
    val userInfo: StateFlow<UserInfo> = _infoUsuario.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _estaCargando.asStateFlow()

    init {
        // Inicializar con valores seguros por defecto
        _estaLogueado.value = false
        _infoUsuario.value = UserInfo("", "", false)

        // Cargar estado real de forma asíncrona
        viewModelScope.launch {
            try {
                val estaLogueado = preferenciasUsuario.isLoggedIn()
                val infoUsuario = preferenciasUsuario.getUserInfo()
                _estaLogueado.value = estaLogueado
                _infoUsuario.value = infoUsuario
            } catch (e: Exception) {
                // En caso de error, mantener valores por defecto
                // No hacer nada, ya están establecidos arriba
            }
        }
    }

    // Funciones helper para validaciones
    private fun validarCamposVacios(vararg campos: String): String? {
        return if (campos.any { it.isBlank() }) "Por favor completa todos los campos" else null
    }

    private fun validarEmail(email: String): String? {
        return if (!esEmailValido(email)) "Por favor ingresa un email válido" else null
    }

    private fun validarPassword(password: String): String? {
        return if (password.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
    }

    private fun validarConfirmacionPassword(password: String, confirmacion: String): String? {
        return if (password != confirmacion) "Las contraseñas no coinciden" else null
    }

    /**
     * Realiza el login del usuario
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null

            try {
                // Validaciones
                val errorValidacion = validarCamposVacios(email, password)
                    ?: validarEmail(email)

                if (errorValidacion != null) {
                    _mensajeError.value = errorValidacion
                    return@launch
                }

                // Intentar login
                if (preferenciasUsuario.loginUser(email, password)) {
                    _estaLogueado.value = true
                    _infoUsuario.value = preferenciasUsuario.getUserInfo()
                } else {
                    _mensajeError.value = "Email o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al iniciar sesión: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Registra un nuevo usuario
     */
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null

            try {
                // Validaciones
                val errorValidacion = validarCamposVacios(name, email, password, confirmPassword)
                    ?: validarEmail(email)
                    ?: validarPassword(password)
                    ?: validarConfirmacionPassword(password, confirmPassword)

                if (errorValidacion != null) {
                    _mensajeError.value = errorValidacion
                    return@launch
                }

                // Intentar registro
                if (preferenciasUsuario.registerUser(name, email, password)) {
                    _estaLogueado.value = true
                    _infoUsuario.value = preferenciasUsuario.getUserInfo()
                } else {
                    _mensajeError.value = "Ya existe una cuenta con este email"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al registrar usuario: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    fun logout() {
        viewModelScope.launch {
            preferenciasUsuario.logout()
            _estaLogueado.value = false
            _infoUsuario.value = UserInfo("", "", false)
            _mensajeError.value = null
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _mensajeError.value = null
    }

    /**
     * Valida el formato de email usando una expresión regular simple
     */
    private fun esEmailValido(email: String): Boolean {
        val patronEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return patronEmail.matches(email)
    }

    /**
     * Verifica si el usuario está autenticado al iniciar la app
     */
    fun checkAuthStatus() {
        viewModelScope.launch {
            _estaLogueado.value = preferenciasUsuario.isLoggedIn()
            _infoUsuario.value = preferenciasUsuario.getUserInfo()
        }
    }
}
