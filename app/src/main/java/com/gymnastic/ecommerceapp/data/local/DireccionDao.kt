package com.gymnastic.ecommerceapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con la tabla de direcciones
 *
 * Maneja todas las operaciones CRUD para direcciones guardadas.
 * Incluye queries específicas para obtener direcciones por usuario y dirección predeterminada.
 */
@Dao
interface DireccionDao {

    /**
     * Obtiene todas las direcciones de un usuario específico
     */
    @Query("SELECT * FROM direcciones WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun getDireccionesByUsuario(usuarioId: String): Flow<List<Direccion>>

    /**
     * Obtiene todas las direcciones del usuario principal
     */
    @Query("SELECT * FROM direcciones WHERE usuarioId = 'usuario_principal' ORDER BY fechaCreacion DESC")
    fun getDireccionesUsuarioPrincipal(): Flow<List<Direccion>>

    /**
     * Obtiene la dirección predeterminada del usuario
     */
    @Query("SELECT * FROM direcciones WHERE usuarioId = :usuarioId AND esPredeterminada = 1 LIMIT 1")
    suspend fun getDireccionPredeterminada(usuarioId: String): Direccion?

    /**
     * Obtiene la dirección predeterminada del usuario principal
     */
    @Query("SELECT * FROM direcciones WHERE usuarioId = 'usuario_principal' AND esPredeterminada = 1 LIMIT 1")
    suspend fun getDireccionPredeterminadaUsuarioPrincipal(): Direccion?

    /**
     * Obtiene una dirección por su ID
     */
    @Query("SELECT * FROM direcciones WHERE id = :id")
    suspend fun getDireccionById(id: String): Direccion?

    /**
     * Inserta una nueva dirección
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDireccion(direccion: Direccion)

    /**
     * Actualiza una dirección existente
     */
    @Update
    suspend fun updateDireccion(direccion: Direccion)

    /**
     * Elimina una dirección
     */
    @Delete
    suspend fun deleteDireccion(direccion: Direccion)

    /**
     * Elimina una dirección por su ID
     */
    @Query("DELETE FROM direcciones WHERE id = :id")
    suspend fun deleteDireccionById(id: String)

    /**
     * Elimina todas las direcciones de un usuario
     */
    @Query("DELETE FROM direcciones WHERE usuarioId = :usuarioId")
    suspend fun deleteDireccionesByUsuario(usuarioId: String)

    /**
     * Establece una dirección como predeterminada
     * Primero quita el flag de predeterminada de todas las direcciones del usuario,
     * luego establece la dirección específica como predeterminada
     */
    @Transaction
    suspend fun establecerDireccionPredeterminada(usuarioId: String, direccionId: String) {
        // Quitar flag de predeterminada de todas las direcciones del usuario
        quitarPredeterminadaDeTodas(usuarioId)
        // Establecer la dirección específica como predeterminada
        establecerPredeterminada(direccionId)
    }

    /**
     * Quita el flag de predeterminada de todas las direcciones del usuario
     */
    @Query("UPDATE direcciones SET esPredeterminada = 0, fechaUltimaActualizacion = :timestamp WHERE usuarioId = :usuarioId")
    suspend fun quitarPredeterminadaDeTodas(usuarioId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Establece una dirección específica como predeterminada
     */
    @Query("UPDATE direcciones SET esPredeterminada = 1, fechaUltimaActualizacion = :timestamp WHERE id = :direccionId")
    suspend fun establecerPredeterminada(direccionId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Cuenta el número de direcciones de un usuario
     */
    @Query("SELECT COUNT(*) FROM direcciones WHERE usuarioId = :usuarioId")
    suspend fun contarDireccionesByUsuario(usuarioId: String): Int
}
