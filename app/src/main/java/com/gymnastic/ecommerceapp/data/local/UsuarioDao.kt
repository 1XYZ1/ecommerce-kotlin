package com.gymnastic.ecommerceapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con la tabla de usuarios
 *
 * Maneja todas las operaciones CRUD para usuarios usando Room Database.
 * Incluye queries específicas para autenticación y gestión de usuarios.
 */
@Dao
interface UsuarioDao {

    /**
     * Obtiene todos los usuarios (por ahora solo habrá uno)
     */
    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<Usuario>>

    /**
     * Obtiene un usuario por su ID
     */
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioById(id: String): Usuario?

    /**
     * Obtiene el usuario principal (el único usuario del sistema)
     */
    @Query("SELECT * FROM usuarios WHERE id = 'usuario_principal'")
    suspend fun getUsuarioPrincipal(): Usuario?

    /**
     * Obtiene el usuario principal como Flow para observación reactiva
     */
    @Query("SELECT * FROM usuarios WHERE id = 'usuario_principal'")
    fun getUsuarioPrincipalFlow(): Flow<Usuario?>

    /**
     * Inserta un nuevo usuario
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: Usuario)

    /**
     * Actualiza un usuario existente
     */
    @Update
    suspend fun updateUsuario(usuario: Usuario)

    /**
     * Elimina un usuario
     */
    @Delete
    suspend fun deleteUsuario(usuario: Usuario)

    /**
     * Elimina todos los usuarios
     */
    @Query("DELETE FROM usuarios")
    suspend fun deleteAllUsuarios()

    /**
     * Actualiza el estado de login del usuario principal
     */
    @Query("UPDATE usuarios SET estaLogueado = :estaLogueado, fechaUltimaActualizacion = :timestamp WHERE id = 'usuario_principal'")
    suspend fun updateLoginStatus(estaLogueado: Boolean, timestamp: Long = System.currentTimeMillis())

    /**
     * Verifica si existe un usuario con el email dado
     */
    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    suspend fun existeUsuarioConEmail(email: String): Int
}
