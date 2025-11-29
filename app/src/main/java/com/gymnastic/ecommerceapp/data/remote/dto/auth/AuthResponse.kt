package com.gymnastic.ecommerceapp.data.remote.dto.auth

import com.google.gson.annotations.SerializedName
import com.gymnastic.ecommerceapp.domain.Usuario

/**
 * DTO para usuario dentro de AuthResponse
 */
data class UserDto(
    val id: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val isActive: Boolean? = null,
    val roles: List<String>? = null
)

/**
 * DTO para la respuesta de autenticación
 *
 * La API devuelve un objeto "user" anidado con los datos del usuario
 */
data class AuthResponse(
    val user: UserDto? = null,
    val token: String? = null
)

/**
 * Extension para verificar si el usuario es admin
 */
fun AuthResponse.esAdmin(): Boolean {
    return user?.roles?.contains("admin") ?: false
}

/**
 * Convierte AuthResponse (DTO de API) a Usuario (modelo de dominio)
 */
fun AuthResponse.toDomain(): Usuario {
    android.util.Log.d("AuthResponse", "Convirtiendo AuthResponse a Usuario:")
    android.util.Log.d("AuthResponse", "  user: $user")
    android.util.Log.d("AuthResponse", "  user.id: ${user?.id}")
    android.util.Log.d("AuthResponse", "  user.fullName: ${user?.fullName}")
    android.util.Log.d("AuthResponse", "  user.email: ${user?.email}")
    android.util.Log.d("AuthResponse", "  user.roles: ${user?.roles}")
    android.util.Log.d("AuthResponse", "  esAdmin: ${esAdmin()}")

    val usuario = Usuario(
        id = user?.id ?: "",
        nombre = user?.fullName ?: "Usuario",
        email = user?.email ?: "",
        esAdmin = esAdmin(),
        estaActivo = user?.isActive ?: true
    )

    android.util.Log.d("AuthResponse", "Usuario creado: nombre=${usuario.nombre}, email=${usuario.email}, esAdmin=${usuario.esAdmin}")
    return usuario
}
