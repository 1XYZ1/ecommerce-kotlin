package com.gymnastic.ecommerceapp.data

import com.gymnastic.ecommerceapp.data.local.AppDb
import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.data.local.Direccion
import com.gymnastic.ecommerceapp.data.local.Usuario
import com.gymnastic.ecommerceapp.domain.Product
import kotlinx.coroutines.flow.Flow

class Repository(private val database: AppDb) {
    private val cartDao = database.cartDao()
    private val usuarioDao = database.usuarioDao()
    private val direccionDao = database.direccionDao()

    // Product operations
    fun getAllProducts(): List<Product> = ProductCatalog.products

    fun getProductById(id: String): Product? = ProductCatalog.products.find { it.id == id }

    // Cart operations
    fun getAllCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun addToCart(product: Product) {
        val existingItem = cartDao.getCartItem(product.id)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.updateCartItem(updatedItem)
        } else {
            val newItem = CartItem(
                productId = product.id,
                productName = product.name,
                productPrice = product.price,
                productImageUrl = product.imageUrl,
                quantity = 1
            )
            cartDao.insertCartItem(newItem)
        }
    }

    suspend fun removeFromCart(productId: String) {
        val item = cartDao.getCartItem(productId)
        item?.let { cartDao.deleteCartItem(it) }
    }

    suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        val item = cartDao.getCartItem(productId)
        item?.let {
            if (quantity <= 0) {
                cartDao.deleteCartItem(it)
            } else {
                cartDao.updateCartItem(it.copy(quantity = quantity))
            }
        }
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    // ========== OPERACIONES DE USUARIO ==========

    /**
     * Obtiene el usuario principal
     */
    suspend fun getUsuarioPrincipal(): Usuario? {
        return usuarioDao.getUsuarioPrincipal()
    }

    /**
     * Obtiene el usuario principal como Flow
     */
    fun getUsuarioPrincipalFlow(): Flow<Usuario?> {
        return usuarioDao.getUsuarioPrincipalFlow()
    }

    /**
     * Inserta o actualiza el usuario principal
     */
    suspend fun insertOrUpdateUsuario(usuario: Usuario) {
        usuarioDao.insertUsuario(usuario)
    }

    /**
     * Actualiza el estado de login del usuario
     */
    suspend fun updateLoginStatus(estaLogueado: Boolean) {
        usuarioDao.updateLoginStatus(estaLogueado)
    }

    // ========== OPERACIONES DE DIRECCIONES ==========

    /**
     * Obtiene todas las direcciones del usuario principal
     */
    fun getDireccionesUsuarioPrincipal(): Flow<List<Direccion>> {
        return direccionDao.getDireccionesUsuarioPrincipal()
    }

    /**
     * Obtiene la dirección predeterminada del usuario principal
     */
    suspend fun getDireccionPredeterminadaUsuarioPrincipal(): Direccion? {
        return direccionDao.getDireccionPredeterminadaUsuarioPrincipal()
    }

    /**
     * Obtiene una dirección por su ID
     */
    suspend fun getDireccionById(id: String): Direccion? {
        return direccionDao.getDireccionById(id)
    }

    /**
     * Guarda una nueva dirección
     */
    suspend fun guardarDireccion(direccion: Direccion) {
        // Si se marca como predeterminada, quitar el flag de las otras direcciones
        if (direccion.esPredeterminada) {
            direccionDao.quitarPredeterminadaDeTodas(direccion.usuarioId)
        }
        direccionDao.insertDireccion(direccion)
    }

    /**
     * Actualiza una dirección existente
     */
    suspend fun actualizarDireccion(direccion: Direccion) {
        // Si se marca como predeterminada, quitar el flag de las otras direcciones
        if (direccion.esPredeterminada) {
            direccionDao.quitarPredeterminadaDeTodas(direccion.usuarioId)
        }
        direccionDao.updateDireccion(direccion)
    }

    /**
     * Elimina una dirección
     */
    suspend fun eliminarDireccion(direccionId: String) {
        direccionDao.deleteDireccionById(direccionId)
    }

    /**
     * Establece una dirección como predeterminada
     */
    suspend fun establecerDireccionPredeterminada(direccionId: String) {
        direccionDao.establecerDireccionPredeterminada("usuario_principal", direccionId)
    }

    /**
     * Cuenta el número de direcciones del usuario principal
     */
    suspend fun contarDireccionesUsuarioPrincipal(): Int {
        return direccionDao.contarDireccionesByUsuario("usuario_principal")
    }
}
