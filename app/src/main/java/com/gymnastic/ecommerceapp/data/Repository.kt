package com.gymnastic.ecommerceapp.data

import com.gymnastic.ecommerceapp.data.local.AppDb
import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.data.local.Direccion
import com.gymnastic.ecommerceapp.data.local.Usuario
import com.gymnastic.ecommerceapp.domain.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repository simplificado para estudiantes
 *
 * Este Repository actúa como una capa simple entre los ViewModels y la base de datos.
 * Su propósito es centralizar las operaciones de datos y hacer el código más fácil de entender.
 *
 * IMPORTANTE: En aplicaciones reales, el Repository es más complejo porque maneja
 * múltiples fuentes de datos (API, base de datos local, cache). Aquí lo mantenemos
 * simple para que los estudiantes entiendan el concepto básico.
 */
class Repository(private val baseDeDatos: AppDb) {

    // Acceso directo a los DAOs (Data Access Objects)
    // Los DAOs son las clases que manejan las operaciones de base de datos
    private val daoCarrito = baseDeDatos.cartDao()
    private val daoUsuario = baseDeDatos.usuarioDao()
    private val daoDireccion = baseDeDatos.direccionDao()

    // ========== OPERACIONES DE PRODUCTOS ==========

    /**
     * Obtiene todos los productos disponibles
     *
     * Los productos están hardcodeados en ProductCatalog para simplificar.
     * En una app real, vendrían de una API o base de datos.
     */
    fun obtenerTodosLosProductos(): List<Product> = ProductCatalog.products

    /**
     * Busca un producto por su ID
     */
    fun obtenerProductoPorId(id: String): Product? = ProductCatalog.products.find { it.id == id }

    // ========== OPERACIONES DEL CARRITO ==========

    /**
     * Obtiene todos los items del carrito como Flow
     *
     * Flow es un stream de datos que se actualiza automáticamente cuando
     * cambian los datos en la base de datos. La UI puede observar este Flow
     * y actualizarse automáticamente.
     */
    fun obtenerItemsDelCarrito(): Flow<List<CartItem>> = daoCarrito.getAllCartItems()

    /**
     * Agrega un producto al carrito
     *
     * Si el producto ya existe, incrementa la cantidad.
     * Si no existe, lo agrega con cantidad 1.
     */
    suspend fun agregarAlCarrito(producto: Product) {
        val itemExistente = daoCarrito.getCartItem(producto.id)
        if (itemExistente != null) {
            // El producto ya está en el carrito, incrementar cantidad
            val itemActualizado = itemExistente.copy(quantity = itemExistente.quantity + 1)
            daoCarrito.updateCartItem(itemActualizado)
        } else {
            // El producto no está en el carrito, agregarlo nuevo
            val nuevoItem = CartItem(
                productId = producto.id,
                productName = producto.name,
                productPrice = producto.price,
                productImageUrl = producto.imageUrl,
                quantity = 1
            )
            daoCarrito.insertCartItem(nuevoItem)
        }
    }

    /**
     * Elimina un producto del carrito completamente
     */
    suspend fun eliminarDelCarrito(idProducto: String) {
        val item = daoCarrito.getCartItem(idProducto)
        item?.let { daoCarrito.deleteCartItem(it) }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito
     *
     * Si la cantidad es 0 o menor, elimina el producto del carrito.
     */
    suspend fun actualizarCantidadEnCarrito(idProducto: String, cantidad: Int) {
        val item = daoCarrito.getCartItem(idProducto)
        item?.let {
            if (cantidad <= 0) {
                // Si la cantidad es 0 o menor, eliminar del carrito
                daoCarrito.deleteCartItem(it)
            } else {
                // Actualizar la cantidad
                daoCarrito.updateCartItem(it.copy(quantity = cantidad))
            }
        }
    }

    /**
     * Vacía completamente el carrito
     */
    suspend fun vaciarCarrito() {
        daoCarrito.clearCart()
    }

    // ========== OPERACIONES DE USUARIO ==========

    /**
     * Obtiene el usuario principal de la aplicación
     *
     * En esta app simplificada solo hay un usuario por aplicación.
     */
    suspend fun obtenerUsuarioPrincipal(): Usuario? {
        return daoUsuario.getUsuarioPrincipal()
    }

    /**
     * Obtiene el usuario principal como Flow para observación reactiva
     */
    fun obtenerUsuarioPrincipalFlow(): Flow<Usuario?> {
        return daoUsuario.getUsuarioPrincipalFlow()
    }

    /**
     * Guarda o actualiza el usuario principal
     */
    suspend fun guardarUsuario(usuario: Usuario) {
        daoUsuario.insertUsuario(usuario)
    }

    /**
     * Actualiza solo el estado de login del usuario
     */
    suspend fun actualizarEstadoLogin(estaLogueado: Boolean) {
        daoUsuario.updateLoginStatus(estaLogueado)
    }

    // ========== OPERACIONES DE DIRECCIONES ==========

    /**
     * Obtiene todas las direcciones del usuario principal
     */
    fun obtenerDireccionesUsuario(): Flow<List<Direccion>> {
        return daoDireccion.getDireccionesUsuarioPrincipal()
    }

    /**
     * Obtiene la dirección predeterminada del usuario
     */
    suspend fun obtenerDireccionPredeterminada(): Direccion? {
        return daoDireccion.getDireccionPredeterminadaUsuarioPrincipal()
    }

    /**
     * Busca una dirección por su ID
     */
    suspend fun obtenerDireccionPorId(id: String): Direccion? {
        return daoDireccion.getDireccionById(id)
    }

    /**
     * Guarda una nueva dirección
     *
     * Si se marca como predeterminada, quita el flag de las otras direcciones
     * para asegurar que solo haya una dirección predeterminada.
     */
    suspend fun guardarDireccion(direccion: Direccion) {
        if (direccion.esPredeterminada) {
            // Quitar el flag de predeterminada de todas las otras direcciones
            daoDireccion.quitarPredeterminadaDeTodas(direccion.usuarioId)
        }
        daoDireccion.insertDireccion(direccion)
    }

    /**
     * Actualiza una dirección existente
     */
    suspend fun actualizarDireccion(direccion: Direccion) {
        if (direccion.esPredeterminada) {
            // Quitar el flag de predeterminada de todas las otras direcciones
            daoDireccion.quitarPredeterminadaDeTodas(direccion.usuarioId)
        }
        daoDireccion.updateDireccion(direccion)
    }

    /**
     * Elimina una dirección por su ID
     */
    suspend fun eliminarDireccion(idDireccion: String) {
        daoDireccion.deleteDireccionById(idDireccion)
    }

    /**
     * Establece una dirección como predeterminada
     */
    suspend fun establecerDireccionPredeterminada(idDireccion: String) {
        daoDireccion.establecerDireccionPredeterminada("usuario_principal", idDireccion)
    }

    /**
     * Cuenta cuántas direcciones tiene el usuario
     */
    suspend fun contarDireccionesUsuario(): Int {
        return daoDireccion.contarDireccionesUsuario()
    }
}
