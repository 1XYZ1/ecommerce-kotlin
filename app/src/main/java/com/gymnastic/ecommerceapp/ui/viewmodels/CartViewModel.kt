package com.gymnastic.ecommerceapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel simplificado para manejar el carrito de compras
 *
 * Este ViewModel maneja todas las operaciones relacionadas con el carrito:
 * - Agregar productos
 * - Eliminar productos
 * - Actualizar cantidades
 * - Vaciar carrito
 * - Obtener productos disponibles
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - Flow: Stream de datos que se actualiza automáticamente cuando cambian los datos
 * - viewModelScope: Scope de corrutinas que se cancela cuando el ViewModel se destruye
 * - suspend functions: Funciones que pueden pausarse y reanudarse (operaciones de BD)
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repositorio: Repository
) : ViewModel() {

    // ========== ESTADOS OBSERVABLES ==========

    /**
     * Flow que emite la lista actual de items en el carrito
     *
     * La UI puede observar este Flow y actualizarse automáticamente
     * cuando cambien los items del carrito en la base de datos.
     */
    val itemsDelCarrito: Flow<List<CartItem>> = repositorio.obtenerItemsDelCarrito()

    // ========== OPERACIONES DEL CARRITO ==========

    /**
     * Agrega un producto al carrito
     *
     * Si el producto ya existe en el carrito, incrementa la cantidad.
     * Si no existe, lo agrega con cantidad 1.
     *
     * @param producto El producto a agregar al carrito
     */
    fun agregarAlCarrito(producto: Product) {
        viewModelScope.launch {
            repositorio.agregarAlCarrito(producto)
        }
    }

    /**
     * Agrega un producto al carrito con una cantidad específica
     *
     * Útil cuando el usuario selecciona una cantidad específica
     * desde la pantalla de detalles del producto.
     *
     * @param producto El producto a agregar
     * @param cantidad La cantidad a agregar
     */
    fun agregarAlCarritoConCantidad(producto: Product, cantidad: Int) {
        viewModelScope.launch {
            // Repetir la operación de agregar según la cantidad solicitada
            repeat(cantidad) {
                repositorio.agregarAlCarrito(producto)
            }
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito
     *
     * Si la cantidad es 0 o menor, elimina el producto del carrito.
     *
     * @param idProducto ID del producto a actualizar
     * @param cantidad Nueva cantidad
     */
    fun actualizarCantidad(idProducto: String, cantidad: Int) {
        viewModelScope.launch {
            repositorio.actualizarCantidadEnCarrito(idProducto, cantidad)
        }
    }

    /**
     * Elimina completamente un producto del carrito
     *
     * @param idProducto ID del producto a eliminar
     */
    fun eliminarDelCarrito(idProducto: String) {
        viewModelScope.launch {
            repositorio.eliminarDelCarrito(idProducto)
        }
    }

    /**
     * Vacía completamente el carrito
     *
     * Se usa después de completar una compra exitosa.
     */
    fun vaciarCarrito() {
        viewModelScope.launch {
            repositorio.vaciarCarrito()
        }
    }

    // ========== OPERACIONES DE PRODUCTOS ==========

    /**
     * Busca un producto por su ID
     *
     * @param idProducto ID del producto a buscar
     * @return El producto encontrado o null si no existe
     */
    fun obtenerProductoPorId(idProducto: String): Product? {
        return repositorio.obtenerProductoPorId(idProducto)
    }

    /**
     * Obtiene todos los productos disponibles
     *
     * @return Lista de todos los productos en el catálogo
     */
    fun obtenerTodosLosProductos(): List<Product> {
        return repositorio.obtenerTodosLosProductos()
    }
}