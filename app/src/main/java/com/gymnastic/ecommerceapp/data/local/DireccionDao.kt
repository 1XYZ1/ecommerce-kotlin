package com.gymnastic.ecommerceapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO simplificado para operaciones con direcciones
 *
 * Este DAO maneja todas las operaciones de base de datos relacionadas
 * con direcciones guardadas. Se simplificó eliminando queries redundantes
 * y manteniendo solo las funciones esenciales.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - @Transaction: Agrupa múltiples operaciones en una transacción atómica
 * - Foreign Key: Relación entre direcciones y usuarios
 * - esPredeterminada: Solo una dirección puede ser predeterminada por usuario
 * - ORDER BY: Ordena resultados por fecha de creación (más recientes primero)
 */
@Dao
interface DireccionDao {

    // ========== CONSULTAS PRINCIPALES ==========

    /**
     * Obtiene todas las direcciones del usuario principal
     *
     * Las direcciones se ordenan por fecha de creación descendente
     * (las más recientes aparecen primero).
     */
    @Query("SELECT * FROM direcciones WHERE usuarioId = 'usuario_principal' ORDER BY fechaCreacion DESC")
    fun getDireccionesUsuarioPrincipal(): Flow<List<Direccion>>

    /**
     * Obtiene la dirección predeterminada del usuario principal
     *
     * Solo puede haber una dirección predeterminada por usuario.
     * LIMIT 1 asegura que solo devuelva una dirección.
     */
    @Query("SELECT * FROM direcciones WHERE usuarioId = 'usuario_principal' AND esPredeterminada = 1 LIMIT 1")
    suspend fun getDireccionPredeterminadaUsuarioPrincipal(): Direccion?

    /**
     * Busca una dirección por su ID
     *
     * @param id ID único de la dirección
     * @return La dirección encontrada o null si no existe
     */
    @Query("SELECT * FROM direcciones WHERE id = :id")
    suspend fun getDireccionById(id: String): Direccion?

    // ========== OPERACIONES CRUD ==========

    /**
     * Inserta una nueva dirección
     *
     * Si ya existe una dirección con el mismo ID, la reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDireccion(direccion: Direccion)

    /**
     * Actualiza una dirección existente
     *
     * Solo actualiza los campos que han cambiado.
     */
    @Update
    suspend fun updateDireccion(direccion: Direccion)

    /**
     * Elimina una dirección por su ID
     *
     * Elimina completamente la dirección de la base de datos.
     */
    @Query("DELETE FROM direcciones WHERE id = :id")
    suspend fun deleteDireccionById(id: String)

    // ========== OPERACIONES DE PREDETERMINADA ==========

    /**
     * Establece una dirección como predeterminada
     *
     * Esta operación es atómica (@Transaction):
     * 1. Quita el flag de predeterminada de todas las direcciones del usuario
     * 2. Establece la dirección específica como predeterminada
     *
     * Esto asegura que solo haya una dirección predeterminada por usuario.
     */
    @Transaction
    suspend fun establecerDireccionPredeterminada(usuarioId: String, direccionId: String) {
        // Paso 1: Quitar flag de predeterminada de todas las direcciones
        quitarPredeterminadaDeTodas(usuarioId)
        // Paso 2: Establecer la dirección específica como predeterminada
        establecerPredeterminada(direccionId)
    }

    /**
     * Quita el flag de predeterminada de todas las direcciones del usuario
     *
     * Se usa internamente por establecerDireccionPredeterminada.
     */
    @Query("UPDATE direcciones SET esPredeterminada = 0, fechaUltimaActualizacion = :timestamp WHERE usuarioId = :usuarioId")
    suspend fun quitarPredeterminadaDeTodas(usuarioId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Establece una dirección específica como predeterminada
     *
     * Se usa internamente por establecerDireccionPredeterminada.
     */
    @Query("UPDATE direcciones SET esPredeterminada = 1, fechaUltimaActualizacion = :timestamp WHERE id = :direccionId")
    suspend fun establecerPredeterminada(direccionId: String, timestamp: Long = System.currentTimeMillis())

    // ========== OPERACIONES DE UTILIDAD ==========

    /**
     * Cuenta cuántas direcciones tiene el usuario principal
     *
     * Útil para validaciones o mostrar estadísticas.
     */
    @Query("SELECT COUNT(*) FROM direcciones WHERE usuarioId = 'usuario_principal'")
    suspend fun contarDireccionesUsuario(): Int
}
