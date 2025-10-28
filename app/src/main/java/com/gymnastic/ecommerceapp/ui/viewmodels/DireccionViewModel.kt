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
 * ViewModel simplificado para manejar direcciones guardadas
 *
 * Este ViewModel maneja todas las operaciones relacionadas con direcciones:
 * - Cargar direcciones guardadas
 * - Agregar nuevas direcciones
 * - Actualizar direcciones existentes
 * - Eliminar direcciones
 * - Establecer dirección predeterminada
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - stateIn: Convierte un Flow en StateFlow con un valor inicial
 * - SharingStarted.WhileSubscribed: Solo mantiene el Flow activo mientras hay observadores
 * - Validaciones: Verificar datos antes de guardar en la base de datos
 */
@HiltViewModel
class DireccionViewModel @Inject constructor(
    private val repositorio: Repository
) : ViewModel() {

    // ========== ESTADOS OBSERVABLES ==========

    /**
     * Lista de direcciones del usuario
     *
     * Se actualiza automáticamente cuando cambian las direcciones en la base de datos.
     * stateIn convierte el Flow en StateFlow para facilitar su uso en la UI.
     */
    val direcciones: StateFlow<List<Direccion>> = repositorio.obtenerDireccionesUsuario()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Solo activo mientras hay observadores
            initialValue = emptyList()
        )

    /**
     * Dirección predeterminada del usuario
     */
    private val _direccionPredeterminada = MutableStateFlow<Direccion?>(null)
    val direccionPredeterminada: StateFlow<Direccion?> = _direccionPredeterminada.asStateFlow()

    /**
     * Estado de carga para mostrar spinners
     */
    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    /**
     * Mensaje de error para mostrar en la UI
     */
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    /**
     * Mensaje de éxito para mostrar en la UI
     */
    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    // ========== INICIALIZACIÓN ==========

    init {
        // Al crear el ViewModel, cargar la dirección predeterminada
        cargarDireccionPredeterminada()
    }

    /**
     * Carga la dirección predeterminada del usuario
     */
    private fun cargarDireccionPredeterminada() {
        viewModelScope.launch {
            try {
                val direccion = repositorio.obtenerDireccionPredeterminada()
                _direccionPredeterminada.value = direccion
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar dirección predeterminada: ${e.message}"
            }
        }
    }

    // ========== OPERACIONES CRUD ==========

    /**
     * Guarda una nueva dirección
     *
     * @param nombreCompleto nombre completo del destinatario
     * @param telefono número de teléfono
     * @param direccionCompleta dirección completa
     * @param esPredeterminada si debe ser la dirección predeterminada
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

                // Validar campos obligatorios
                val errorValidacion = validarCampos(nombreCompleto, telefono, direccionCompleta)
                if (errorValidacion != null) {
                    _mensajeError.value = errorValidacion
                    return@launch
                }

                // Crear nueva dirección
                val nuevaDireccion = Direccion(
                    usuarioId = "usuario_principal",
                    nombreCompleto = nombreCompleto.trim(),
                    telefono = telefono.trim(),
                    direccionCompleta = direccionCompleta.trim(),
                    esPredeterminada = esPredeterminada
                )

                // Guardar en la base de datos
                repositorio.guardarDireccion(nuevaDireccion)
                _mensajeExito.value = "Dirección guardada exitosamente"

                // Recargar dirección predeterminada si es necesario
                if (esPredeterminada) {
                    cargarDireccionPredeterminada()
                }

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

                // Validar campos obligatorios
                val errorValidacion = validarCampos(nombreCompleto, telefono, direccionCompleta)
                if (errorValidacion != null) {
                    _mensajeError.value = errorValidacion
                    return@launch
                }

                // Crear dirección actualizada
                val direccionActualizada = Direccion(
                    id = direccionId,
                    usuarioId = "usuario_principal",
                    nombreCompleto = nombreCompleto.trim(),
                    telefono = telefono.trim(),
                    direccionCompleta = direccionCompleta.trim(),
                    esPredeterminada = esPredeterminada
                )

                // Actualizar en la base de datos
                repositorio.actualizarDireccion(direccionActualizada)
                _mensajeExito.value = "Dirección actualizada exitosamente"

                // Recargar dirección predeterminada si es necesario
                if (esPredeterminada) {
                    cargarDireccionPredeterminada()
                }

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

                repositorio.eliminarDireccion(direccionId)
                _mensajeExito.value = "Dirección eliminada exitosamente"

                // Recargar dirección predeterminada por si se eliminó la predeterminada
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

                repositorio.establecerDireccionPredeterminada(direccionId)
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

    // ========== FUNCIONES DE UTILIDAD ==========

    /**
     * Busca una dirección por su ID
     */
    suspend fun obtenerDireccionPorId(direccionId: String): Direccion? {
        return try {
            repositorio.obtenerDireccionPorId(direccionId)
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

    // ========== FUNCIONES DE VALIDACIÓN ==========

    /**
     * Valida todos los campos de una dirección
     */
    private fun validarCampos(nombreCompleto: String, telefono: String, direccionCompleta: String): String? {
        return when {
            nombreCompleto.isBlank() -> "El nombre completo es requerido"
            nombreCompleto.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            telefono.isBlank() -> "El teléfono es requerido"
            telefono.length < 10 -> "El teléfono debe tener al menos 10 dígitos"
            !telefono.all { it.isDigit() } -> "El teléfono solo puede contener números"
            direccionCompleta.isBlank() -> "La dirección es requerida"
            direccionCompleta.length < 10 -> "La dirección debe tener al menos 10 caracteres"
            else -> null
        }
    }
}
