package com.gymnastic.ecommerceapp.data.remote.dto.auth

/**
 * DTO para la petición de registro
 *
 * Se envía al endpoint POST /auth/register
 */
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)
