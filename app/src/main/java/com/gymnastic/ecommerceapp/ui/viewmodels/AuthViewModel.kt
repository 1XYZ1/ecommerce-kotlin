package com.gymnastic.ecommerceapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.UserInfo
import com.gymnastic.ecommerceapp.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para manejar la autenticación del usuario con la API
 *
 * ACTUALIZADO: Ahora usa la API para autenticación en lugar de Room local
 * - Login/Registro hacen peticiones HTTP
 * - Token JWT se guarda automáticamente
 * - Maneja estados Result (Exito/Error/Cargando)
 *
 * CONCEPTOS PARA ESTUDIANTES:
 * - StateFlow: Mantiene un estado actual y notifica cambios a la UI
 * - Result pattern: Maneja éxito/error de forma segura y tipo-safe
 * - viewModelScope: Ejecuta corrutinas que se cancelan al destruir el ViewModel
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repositorio: Repository
) : ViewModel() {

    // ========== ESTADOS PRINCIPALES ==========

    /**
     * Estado que indica si el usuario está logueado
     */
    private val _estaLogueado = MutableStateFlow(false)
    val estaLogueado: StateFlow<Boolean> = _estaLogueado.asStateFlow()

    /**
     * Información del usuario actual
     */
    private val _infoUsuario = MutableStateFlow(UserInfo("", "", false, false))
    val infoUsuario: StateFlow<UserInfo> = _infoUsuario.asStateFlow()

    /**
     * Mensaje de error para mostrar en la UI
     */
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    /**
     * Estado de carga para mostrar spinners
     */
    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    // ========== INICIALIZACIÓN ==========

    init {
        // Verificar si hay sesión válida al iniciar
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "========== INIT: Verificando sesión ==========")
                val tieneToken = repositorio.estaAutenticado()
                Log.d("AuthViewModel", "Tiene token guardado: $tieneToken")

                if (tieneToken) {
                    // Intentar verificar el token con la API
                    Log.d("AuthViewModel", "Verificando estado con API...")
                    when (val resultado = repositorio.verificarEstadoAuth()) {
                        is Result.Exito -> {
                            // Token válido, restaurar sesión
                            _estaLogueado.value = true
                            _infoUsuario.value = UserInfo(
                                nombre = resultado.datos.nombre,
                                email = resultado.datos.email,
                                estaLogueado = true,
                                esAdmin = resultado.datos.esAdmin
                            )
                            Log.d("AuthViewModel", "✓ Sesión restaurada: ${resultado.datos.nombre}")
                            Log.d("AuthViewModel", "  (La sincronización del carrito se ejecutó en verificarEstadoAuth)")
                        }
                        is Result.Error -> {
                            // Token inválido, limpiar
                            Log.d("AuthViewModel", "✗ Token inválido, limpiando sesión")
                            repositorio.cerrarSesion()
                            _estaLogueado.value = false
                            _infoUsuario.value = UserInfo("", "", false, false)
                        }
                        is Result.Cargando -> {}
                    }
                } else {
                    // No hay token, iniciar limpio
                    Log.d("AuthViewModel", "No hay token, iniciando sin autenticar")
                    _estaLogueado.value = false
                    _infoUsuario.value = UserInfo("", "", false, false)
                }
                Log.d("AuthViewModel", "========== FIN INIT ==========")
            } catch (e: Exception) {
                // Si falla, empezar sin autenticar
                Log.e("AuthViewModel", "Error al verificar sesión inicial", e)
                _estaLogueado.value = false
                _infoUsuario.value = UserInfo("", "", false, false)
            }
        }
    }

    // ========== FUNCIONES DE VALIDACIÓN ==========

    /**
     * Valida que todos los campos estén completos
     */
    private fun validarCamposVacios(vararg campos: String): String? {
        return if (campos.any { it.isBlank() }) {
            "Por favor completa todos los campos"
        } else null
    }

    /**
     * Valida el formato del email
     */
    private fun validarEmail(email: String): String? {
        return if (!esEmailValido(email)) {
            "Por favor ingresa un email válido"
        } else null
    }

    /**
     * Valida que la contraseña tenga al menos 6 caracteres
     */
    private fun validarPassword(password: String): String? {
        return if (password.length < 6) {
            "La contraseña debe tener al menos 6 caracteres"
        } else null
    }

    /**
     * Valida que las contraseñas coincidan
     */
    private fun validarConfirmacionPassword(password: String, confirmacion: String): String? {
        return if (password != confirmacion) {
            "Las contraseñas no coinciden"
        } else null
    }

    /**
     * Valida el formato de email usando expresión regular
     */
    private fun esEmailValido(email: String): Boolean {
        val patronEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return patronEmail.matches(email)
    }

    // ========== FUNCIONES PÚBLICAS ==========

    /**
     * Realiza el login del usuario con la API
     *
     * @param email email del usuario
     * @param password contraseña del usuario
     */
    fun iniciarSesion(email: String, password: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null

            try {
                // Validar campos de entrada
                val errorValidacion = validarCamposVacios(email, password)
                    ?: validarEmail(email)

                if (errorValidacion != null) {
                    _mensajeError.value = errorValidacion
                    return@launch
                }

                // Llamar a la API para hacer login
                when (val resultado = repositorio.iniciarSesion(email, password)) {
                    is Result.Exito -> {
                        // Login exitoso
                        Log.d("AuthViewModel", "Login exitoso - Usuario recibido del Repository:")
                        Log.d("AuthViewModel", "  nombre: ${resultado.datos.nombre}")
                        Log.d("AuthViewModel", "  email: ${resultado.datos.email}")
                        Log.d("AuthViewModel", "  esAdmin: ${resultado.datos.esAdmin}")

                        _estaLogueado.value = true

                        val userInfo = UserInfo(
                            nombre = resultado.datos.nombre,
                            email = resultado.datos.email,
                            estaLogueado = true,
                            esAdmin = resultado.datos.esAdmin
                        )

                        Log.d("AuthViewModel", "UserInfo creado:")
                        Log.d("AuthViewModel", "  nombre: ${userInfo.nombre}")
                        Log.d("AuthViewModel", "  email: ${userInfo.email}")
                        Log.d("AuthViewModel", "  esAdmin: ${userInfo.esAdmin}")

                        _infoUsuario.value = userInfo

                        Log.d("AuthViewModel", "Estado final de _infoUsuario.value: ${_infoUsuario.value}")
                    }
                    is Result.Error -> {
                        _mensajeError.value = resultado.mensaje
                    }
                    is Result.Cargando -> {}
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error inesperado: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Registra un nuevo usuario en la API
     *
     * @param nombre nombre completo del usuario
     * @param email email del usuario
     * @param password contraseña del usuario
     * @param confirmarPassword confirmación de la contraseña
     */
    fun registrarUsuario(
        nombre: String,
        email: String,
        password: String,
        confirmarPassword: String
    ) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null

            try {
                // Validar todos los campos
                val errorValidacion = validarCamposVacios(nombre, email, password, confirmarPassword)
                    ?: validarEmail(email)
                    ?: validarPassword(password)
                    ?: validarConfirmacionPassword(password, confirmarPassword)

                if (errorValidacion != null) {
                    _mensajeError.value = errorValidacion
                    return@launch
                }

                // Llamar a la API para registrar
                when (val resultado = repositorio.registrar(nombre, email, password)) {
                    is Result.Exito -> {
                        // Registro exitoso, automáticamente logueado
                        _estaLogueado.value = true
                        _infoUsuario.value = UserInfo(
                            nombre = resultado.datos.nombre,
                            email = resultado.datos.email,
                            estaLogueado = true,
                            esAdmin = resultado.datos.esAdmin
                        )
                    }
                    is Result.Error -> {
                        _mensajeError.value = resultado.mensaje
                    }
                    is Result.Cargando -> {}
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error inesperado: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    fun cerrarSesion() {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "========== CERRANDO SESIÓN ==========")
                Log.d("AuthViewModel", "Estado antes: estaLogueado=${_estaLogueado.value}")
                Log.d("AuthViewModel", "  nombre: ${_infoUsuario.value.nombre}")
                Log.d("AuthViewModel", "  email: ${_infoUsuario.value.email}")

                // Eliminar token del servidor/DataStore
                repositorio.cerrarSesion()
                Log.d("AuthViewModel", "✓ Token eliminado del DataStore")

                // Actualizar estados locales
                _estaLogueado.value = false
                _infoUsuario.value = UserInfo("", "", false, false)
                _mensajeError.value = null

                Log.d("AuthViewModel", "✓ Estados actualizados:")
                Log.d("AuthViewModel", "  estaLogueado=${_estaLogueado.value}")
                Log.d("AuthViewModel", "  infoUsuario=${_infoUsuario.value}")
                Log.d("AuthViewModel", "========== SESIÓN CERRADA ==========")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "✗ Error al cerrar sesión", e)
                _mensajeError.value = "Error al cerrar sesión: ${e.message}"
            }
        }
    }

    /**
     * Limpia el mensaje de error actual
     */
    fun limpiarError() {
        _mensajeError.value = null
    }
}
