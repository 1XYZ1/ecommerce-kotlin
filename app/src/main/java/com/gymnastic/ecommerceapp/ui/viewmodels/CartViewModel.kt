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
 * ViewModel para manejar la lógica del carrito de compras
 *
 * Un ViewModel es una clase que mantiene la lógica de la UI y los datos
 * de forma independiente de los cambios de configuración (como rotación de pantalla).
 *
 * En este caso, encapsula todas las operaciones relacionadas con el carrito:
 * - Agregar productos
 * - Eliminar productos
 * - Actualizar cantidades
 * - Obtener el estado actual del carrito
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    /**
     * Flow que emite la lista actual de ítems en el carrito
     *
     * Flow es un stream de datos reactivo que se actualiza automáticamente
     * cuando cambian los datos en la base de datos Room.
     * La UI puede observar este Flow y actualizarse automáticamente.
     */
    val cartItems: Flow<List<CartItem>> = repository.getAllCartItems()

    /**
     * Agrega un producto al carrito
     *
     * Si el producto ya existe en el carrito, incrementa la cantidad.
     * Si no existe, lo agrega con cantidad 1.
     *
     * @param product El producto a agregar al carrito
     */
    fun addToCart(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }

    /**
     * Agrega un producto al carrito con una cantidad específica
     *
     * @param product El producto a agregar
     * @param quantity La cantidad a agregar
     */
    fun addToCartWithQuantity(product: Product, quantity: Int) {
        viewModelScope.launch {
            repeat(quantity) {
                repository.addToCart(product)
            }
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito
     *
     * Si la cantidad es 0 o menor, elimina el producto del carrito.
     *
     * @param productId ID del producto a actualizar
     * @param quantity Nueva cantidad
     */
    fun updateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, quantity)
        }
    }

    /**
     * Elimina completamente un producto del carrito
     *
     * @param productId ID del producto a eliminar
     */
    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    /**
     * Limpia todo el carrito (se usa después de completar una compra)
     */
    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    /**
     * Obtiene un producto por su ID
     */
    fun getProductById(productId: String) = repository.getProductById(productId)

    /**
     * Obtiene todos los productos disponibles
     */
    fun getAllProducts() = repository.getAllProducts()
}