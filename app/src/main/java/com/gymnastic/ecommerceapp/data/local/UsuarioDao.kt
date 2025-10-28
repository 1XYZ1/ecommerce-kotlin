package com.gymnastic.ecommerceapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO simplificado para operaciones con usuarios
 *
 * Este DAO maneja todas las operaciones de base de datos relacionadas
 * con usuarios. Se simplificó eliminando queries innecesarias y
 * manteniendo solo las funciones esenciales para esta app de estudiantes.
 *
 * CONCEPTOS IMPORTANTES PARA ESTUDIANTES:
 * - En esta app solo hay un usuario por aplicación (simplificado)
 * - Las queries usan 'usuario_principal' como ID fijo
 * - Flow permite observación reactiva de cambios
 * - suspend indica operaciones asíncronas
 */
@Dao
interface UsuarioDao {

    // ========== CONSULTAS PRINCIPALES ==========

    /**
     * Obtiene el usuario principal de la aplicación
     *
     * En esta app simplificada solo hay un usuario por aplicación.
     * Su ID es siempre 'usuario_principal'.
     */
    @Query("SELECT * FROM usuarios WHERE id = 'usuario_principal'")
    suspend fun getUsuarioPrincipal(): Usuario?

    /**
     * Obtiene el usuario principal como Flow para observación reactiva
     *
     * La UI puede observar este Flow y actualizarse automáticamente
     * cuando cambien los datos del usuario.
     */
    @Query("SELECT * FROM usuarios WHERE id = 'usuario_principal'")
    fun getUsuarioPrincipalFlow(): Flow<Usuario?>

    // ========== OPERACIONES CRUD ==========

    /**
     * Inserta o actualiza el usuario principal
     *
     * Si el usuario ya existe, lo reemplaza completamente.
     * Si no existe, lo crea nuevo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)

    /**
     * Actualiza un usuario existente
     *
     * Solo actualiza los campos que han cambiado.
     */
    @Update
    suspend fun updateUsuario(usuario: Usuario)

    /**
     * Actualiza solo el estado de login del usuario principal
     *
     * Esta función es más eficiente que updateUsuario cuando solo
     * necesitamos cambiar el estado de login.
     *
     * @param estaLogueado nuevo estado de login
     * @param timestamp momento de la actualización
     */
    @Query("UPDATE usuarios SET estaLogueado = :estaLogueado, fechaUltimaActualizacion = :timestamp WHERE id = 'usuario_principal'")
    suspend fun updateLoginStatus(estaLogueado: Boolean, timestamp: Long = System.currentTimeMillis())

    // ========== OPERACIONES DE LIMPIEZA ==========

    /**
     * Elimina todos los usuarios
     *
     * Se usa para limpiar completamente la base de datos.
     * Útil para testing o reset completo de la app.
     */
    @Query("DELETE FROM usuarios")
    suspend fun deleteAllUsuarios()
}
