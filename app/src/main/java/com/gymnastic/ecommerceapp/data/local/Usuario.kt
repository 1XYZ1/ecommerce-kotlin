package com.gymnastic.ecommerceapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para almacenar datos del usuario en Room Database
 *
 * Reemplaza el uso de SharedPreferences para datos del usuario.
 * Incluye información básica del usuario y estado de autenticación.
 */
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val id: String = "usuario_principal", // Solo un usuario por ahora
    val nombre: String,
    val email: String,
    val password: String,
    val estaLogueado: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaUltimaActualizacion: Long = System.currentTimeMillis()
)
