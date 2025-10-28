package com.gymnastic.ecommerceapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO simplificado para operaciones del carrito de compras
 *
 * Este DAO maneja todas las operaciones de base de datos relacionadas
 * con el carrito de compras. Se simplificó eliminando queries innecesarias
 * y agregando comentarios educativos en español.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - @Dao: Marca la interfaz como Data Access Object
 * - @Query: Define consultas SQL personalizadas
 * - @Insert, @Update, @Delete: Anotaciones para operaciones CRUD básicas
 * - Flow: Stream reactivo que se actualiza automáticamente
 * - suspend: Funciones que pueden pausarse (operaciones de BD)
 */
@Dao
interface CartDao {

    // ========== CONSULTAS (QUERIES) ==========

    /**
     * Obtiene todos los items del carrito como Flow
     *
     * Flow permite que la UI se actualice automáticamente cuando
     * cambien los datos en la base de datos.
     */
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    /**
     * Busca un item específico del carrito por ID de producto
     *
     * @param productId ID del producto a buscar
     * @return El item del carrito o null si no existe
     */
    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItem(productId: String): CartItem?

    // ========== OPERACIONES CRUD ==========

    /**
     * Inserta un nuevo item en el carrito
     *
     * Si el item ya existe (mismo productId), lo reemplaza completamente.
     * Esto es útil para actualizar cantidades o información del producto.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    /**
     * Actualiza un item existente en el carrito
     *
     * Solo actualiza los campos que han cambiado.
     * El item debe existir previamente en la base de datos.
     */
    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    /**
     * Elimina un item específico del carrito
     *
     * Elimina completamente el item de la base de datos.
     */
    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    /**
     * Vacía completamente el carrito
     *
     * Elimina todos los items del carrito de una vez.
     * Se usa después de completar una compra exitosa.
     */
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
