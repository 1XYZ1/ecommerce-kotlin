package com.gymnastic.ecommerceapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.Direccion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para manejar la gestión de direcciones guardadas
 *
 * Maneja todas las operaciones relacionadas con direcciones:
 * - Cargar direcciones guardadas
 * - Agregar nuevas direcciones
 * - Actualizar direcciones existentes
 * - Eliminar direcciones
 * - Establecer dirección predeterminada
 */
@HiltViewModel
class DireccionViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // Estados principales - usando stateIn para simplificar
    val direcciones: StateFlow<List<Direccion>> = repository.getDireccionesUsuarioPrincipal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _direccionPredeterminada = MutableStateFlow<Direccion?>(null)
    val direccionPredeterminada: StateFlow<Direccion?> = _direccionPredeterminada.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    init {
        cargarDireccionPredeterminada()
    }

    /**
     * Ya no necesitamos cargar direcciones manualmente - se hace automáticamente con stateIn
     */
    fun cargarDirecciones() {
        // Las direcciones se cargan automáticamente con stateIn
        // Este método se mantiene para compatibilidad pero ya no hace nada
    }

    /**
     * Carga la dirección predeterminada del usuario
     */
    private fun cargarDireccionPredeterminada() {
        viewModelScope.launch {
            try {
                val direccion = repository.getDireccionPredeterminadaUsuarioPrincipal()
                _direccionPredeterminada.value = direccion
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar dirección predeterminada: ${e.message}"
            }
        }
    }

    /**
     * Guarda una nueva dirección
     */
    fun guardarDireccion(
        nombreCompleto: String,
        telefono: String,
        direccionCompleta: String,
        esPredeterminada: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                _estaCargando.value = true
                _mensajeError.value = null

                // Validaciones básicas
                if (nombreCompleto.isBlank()) {
                    _mensajeError.value = "El nombre completo es requerido"
                    return@launch
                }
                if (telefono.isBlank()) {
                    _mensajeError.value = "El teléfono es requerido"
                    return@launch
                }
                if (direccionCompleta.isBlank()) {
                    _mensajeError.value = "La dirección es requerida"
                    return@launch
                }

                val nuevaDireccion = Direccion(
                    usuarioId = "usuario_principal",
                    nombreCompleto = nombreCompleto.trim(),
                    telefono = telefono.trim(),
                    direccionCompleta = direccionCompleta.trim(),
                    esPredeterminada = esPredeterminada
                )

                repository.guardarDireccion(nuevaDireccion)
                _mensajeExito.value = "Dirección guardada exitosamente"

                // Recargar dirección predeterminada si es necesario
                cargarDireccionPredeterminada()

            } catch (e: Exception) {
                _mensajeError.value = "Error al guardar dirección: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Actualiza una dirección existente
     */
    fun actualizarDireccion(
        direccionId: String,
        nombreCompleto: String,
        telefono: String,
        direccionCompleta: String,
        esPredeterminada: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                _estaCargando.value = true
                _mensajeError.value = null

                // Validaciones básicas
                if (nombreCompleto.isBlank()) {
                    _mensajeError.value = "El nombre completo es requerido"
                    return@launch
                }
                if (telefono.isBlank()) {
                    _mensajeError.value = "El teléfono es requerido"
                    return@launch
                }
                if (direccionCompleta.isBlank()) {
                    _mensajeError.value = "La dirección es requerida"
                    return@launch
                }

                val direccionActualizada = Direccion(
                    id = direccionId,
                    usuarioId = "usuario_principal",
                    nombreCompleto = nombreCompleto.trim(),
                    telefono = telefono.trim(),
                    direccionCompleta = direccionCompleta.trim(),
                    esPredeterminada = esPredeterminada
                )

                repository.actualizarDireccion(direccionActualizada)
                _mensajeExito.value = "Dirección actualizada exitosamente"

                // Recargar dirección predeterminada si es necesario
                cargarDireccionPredeterminada()

            } catch (e: Exception) {
                _mensajeError.value = "Error al actualizar dirección: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Elimina una dirección
     */
    fun eliminarDireccion(direccionId: String) {
        viewModelScope.launch {
            try {
                _estaCargando.value = true
                _mensajeError.value = null

                repository.eliminarDireccion(direccionId)
                _mensajeExito.value = "Dirección eliminada exitosamente"

                // Recargar dirección predeterminada si es necesario
                cargarDireccionPredeterminada()

            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar dirección: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Establece una dirección como predeterminada
     */
    fun establecerDireccionPredeterminada(direccionId: String) {
        viewModelScope.launch {
            try {
                _estaCargando.value = true
                _mensajeError.value = null

                repository.establecerDireccionPredeterminada(direccionId)
                _mensajeExito.value = "Dirección establecida como predeterminada"

                // Recargar dirección predeterminada
                cargarDireccionPredeterminada()

            } catch (e: Exception) {
                _mensajeError.value = "Error al establecer dirección predeterminada: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    /**
     * Obtiene una dirección por su ID
     */
    suspend fun obtenerDireccionPorId(direccionId: String): Direccion? {
        return try {
            repository.getDireccionById(direccionId)
        } catch (e: Exception) {
            _mensajeError.value = "Error al obtener dirección: ${e.message}"
            null
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun limpiarError() {
        _mensajeError.value = null
    }

    /**
     * Limpia el mensaje de éxito
     */
    fun limpiarMensajeExito() {
        _mensajeExito.value = null
    }

    /**
     * Valida el formato del teléfono
     */
    fun validarTelefono(telefono: String): String? {
        return when {
            telefono.isBlank() -> "El teléfono es requerido"
            telefono.length < 10 -> "El teléfono debe tener al menos 10 dígitos"
            !telefono.all { it.isDigit() } -> "Solo se permiten números"
            else -> null
        }
    }

    /**
     * Valida el formato del nombre completo
     */
    fun validarNombreCompleto(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre completo es requerido"
            nombre.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    /**
     * Valida el formato de la dirección
     */
    fun validarDireccion(direccion: String): String? {
        return when {
            direccion.isBlank() -> "La dirección es requerida"
            direccion.length < 10 -> "La dirección debe tener al menos 10 caracteres"
            else -> null
        }
    }
}
