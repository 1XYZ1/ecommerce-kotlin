package com.gymnastic.ecommerceapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO para acceder a la tabla de productos cacheados
 *
 * Proporciona operaciones CRUD simples para el cache de productos
 */
@Dao
interface ProductDao {

    /**
     * Inserta o actualiza productos en el cache
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<ProductEntity>)

    /**
     * Obtiene todos los productos del cache
     */
    @Query("SELECT * FROM products_cache ORDER BY title ASC")
    suspend fun getAll(): List<ProductEntity>

    /**
     * Obtiene un producto por su ID del cache
     */
    @Query("SELECT * FROM products_cache WHERE id = :id")
    suspend fun getById(id: String): ProductEntity?

    /**
     * Busca productos por nombre en el cache
     */
    @Query("SELECT * FROM products_cache WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<ProductEntity>

    /**
     * Limpia todo el cache de productos
     */
    @Query("DELETE FROM products_cache")
    suspend fun clearAll()

    /**
     * Cuenta cuántos productos hay en el cache
     */
    @Query("SELECT COUNT(*) FROM products_cache")
    suspend fun count(): Int
}
