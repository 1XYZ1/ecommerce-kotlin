package com.gymnastic.ecommerceapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para funcionalidades de administrador
 *
 * Maneja la creación, edición y eliminación de productos
 * Solo accesible para usuarios con rol de admin
 */
@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repositorio: Repository
) : ViewModel() {

    // ========== ESTADOS ==========

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _operacionExitosa = MutableStateFlow(false)
    val operacionExitosa: StateFlow<Boolean> = _operacionExitosa.asStateFlow()

    // ========== OPERACIONES CRUD DE PRODUCTOS ==========

    /**
     * Crea un nuevo producto (solo admin)
     *
     * @param nombre Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio Precio como String (se convierte a Double)
     * @param stock Stock como String (se convierte a Int)
     */
    fun crearProducto(
        nombre: String,
        descripcion: String,
        precio: String,
        stock: String
    ) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null
            _operacionExitosa.value = false

            try {
                // Validaciones de campos
                if (nombre.isBlank() || descripcion.isBlank() || precio.isBlank() || stock.isBlank()) {
                    _mensajeError.value = "Todos los campos son obligatorios"
                    _estaCargando.value = false
                    return@launch
                }

                val precioDouble = precio.toDoubleOrNull()
                val stockInt = stock.toIntOrNull()

                if (precioDouble == null || precioDouble <= 0) {
                    _mensajeError.value = "El precio debe ser un número válido mayor a 0"
                    _estaCargando.value = false
                    return@launch
                }

                if (stockInt == null || stockInt < 0) {
                    _mensajeError.value = "El stock debe ser un número válido mayor o igual a 0"
                    _estaCargando.value = false
                    return@launch
                }

                // Llamar a la API
                when (val resultado = repositorio.crearProducto(
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precioDouble,
                    stock = stockInt
                )) {
                    is Result.Exito -> {
                        _operacionExitosa.value = true
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
     * Actualiza un producto existente (solo admin)
     *
     * @param id ID del producto a actualizar
     * @param nombre Nuevo nombre
     * @param descripcion Nueva descripción
     * @param precio Nuevo precio como String
     * @param stock Nuevo stock como String
     */
    fun actualizarProducto(
        id: String,
        nombre: String,
        descripcion: String,
        precio: String,
        stock: String
    ) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null
            _operacionExitosa.value = false

            try {
                val precioDouble = precio.toDoubleOrNull() ?: 0.0
                val stockInt = stock.toIntOrNull() ?: 0

                if (precioDouble <= 0 || stockInt < 0) {
                    _mensajeError.value = "Precio y stock deben ser valores válidos"
                    _estaCargando.value = false
                    return@launch
                }

                when (val resultado = repositorio.actualizarProducto(
                    id = id,
                    nombre = nombre,
                    descripcion = descripcion,
                    precio = precioDouble,
                    stock = stockInt
                )) {
                    is Result.Exito -> {
                        _operacionExitosa.value = true
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
     * Elimina un producto (solo admin)
     *
     * @param id ID del producto a eliminar
     */
    fun eliminarProducto(id: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeError.value = null

            try {
                when (val resultado = repositorio.eliminarProducto(id)) {
                    is Result.Exito -> {
                        _operacionExitosa.value = true
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
     * Limpia los mensajes de error y éxito
     */
    fun limpiarMensajes() {
        _mensajeError.value = null
        _operacionExitosa.value = false
    }
}
