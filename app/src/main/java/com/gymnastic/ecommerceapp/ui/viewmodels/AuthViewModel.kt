package com.gymnastic.ecommerceapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.UserInfo
import com.gymnastic.ecommerceapp.data.local.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel simplificado para manejar la autenticación del usuario
 *
 * Este ViewModel maneja todo lo relacionado con el login, registro y logout del usuario.
 * Es más simple porque accede directamente al Repository en lugar de usar UserPreferences.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - StateFlow: Es un tipo especial de Flow que mantiene un estado actual
 * - MutableStateFlow: Permite cambiar el valor del estado
 * - asStateFlow(): Convierte el MutableStateFlow en StateFlow (solo lectura para la UI)
 * - viewModelScope: Scope de corrutinas que se cancela cuando el ViewModel se destruye
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repositorio: Repository
) : ViewModel() {

    // ========== ESTADOS PRINCIPALES ==========

    /**
     * Estado que indica si el usuario está logueado
     *
     * La UI puede observar este estado y actualizarse automáticamente
     * cuando cambie el valor.
     */
    private val _estaLogueado = MutableStateFlow(false)
    val estaLogueado: StateFlow<Boolean> = _estaLogueado.asStateFlow()

    /**
     * Información del usuario actual
     *
     * Contiene nombre, email y estado de login del usuario.
     */
    private val _infoUsuario = MutableStateFlow(UserInfo("", "", false))
    val infoUsuario: StateFlow<UserInfo> = _infoUsuario.asStateFlow()

    /**
     * Mensaje de error para mostrar en la UI
     *
     * Si es null, no hay error. Si tiene valor, mostrar el mensaje.
     */
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    /**
     * Estado de carga para mostrar spinners o botones deshabilitados
     */
    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    // ========== INICIALIZACIÓN ==========

    init {
        // Al crear el ViewModel, cargar el estado actual del usuario
        cargarEstadoUsuario()
    }

    /**
     * Carga el estado actual del usuario desde la base de datos
     *
     * Se ejecuta automáticamente cuando se crea el ViewModel.
     */
    private fun cargarEstadoUsuario() {
        viewModelScope.launch {
            try {
                val usuario = repositorio.obtenerUsuarioPrincipal()
                if (usuario != null) {
                    _estaLogueado.value = usuario.estaLogueado
                    _infoUsuario.value = UserInfo(
                        nombre = usuario.nombre,
                        email = usuario.email,
                        estaLogueado = usuario.estaLogueado
                    )
                } else {
                    // No hay usuario registrado
                    _estaLogueado.value = false
                    _infoUsuario.value = UserInfo("", "", false)
                }
            } catch (e: Exception) {
                // En caso de error, mantener valores por defecto
                _estaLogueado.value = false
                _infoUsuario.value = UserInfo("", "", false)
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
     * Realiza el login del usuario
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

                // Buscar usuario en la base de datos
                val usuario = repositorio.obtenerUsuarioPrincipal()

                if (usuario != null && usuario.email == email && usuario.password == password) {
                    // Login exitoso
                    repositorio.actualizarEstadoLogin(true)
                    _estaLogueado.value = true
                    _infoUsuario.value = UserInfo(
                        nombre = usuario.nombre,
                        email = usuario.email,
                        estaLogueado = true
                    )
                } else {
                    // Credenciales incorrectas
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
     *
     * @param nombre nombre del usuario
     * @param email email del usuario
     * @param password contraseña del usuario
     * @param confirmarPassword confirmación de contraseña
     */
    fun registrarUsuario(nombre: String, email: String, password: String, confirmarPassword: String) {
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

                // Verificar si ya existe un usuario
                val usuarioExistente = repositorio.obtenerUsuarioPrincipal()
                if (usuarioExistente != null) {
                    _mensajeError.value = "Ya existe una cuenta registrada"
                    return@launch
                }

                // Crear nuevo usuario
                val nuevoUsuario = Usuario(
                    id = "usuario_principal",
                    nombre = nombre,
                    email = email,
                    password = password,
                    estaLogueado = true
                )

                // Guardar usuario en la base de datos
                repositorio.guardarUsuario(nuevoUsuario)

                // Actualizar estados
                _estaLogueado.value = true
                _infoUsuario.value = UserInfo(
                    nombre = nombre,
                    email = email,
                    estaLogueado = true
                )
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
    fun cerrarSesion() {
        viewModelScope.launch {
            try {
                repositorio.actualizarEstadoLogin(false)
                _estaLogueado.value = false
                _infoUsuario.value = UserInfo("", "", false)
                _mensajeError.value = null
            } catch (e: Exception) {
                _mensajeError.value = "Error al cerrar sesión: ${e.message}"
            }
        }
    }

    /**
     * Limpia el mensaje de error
     *
     * Se llama desde la UI cuando el usuario quiere ocultar el error.
     */
    fun limpiarError() {
        _mensajeError.value = null
    }
}
