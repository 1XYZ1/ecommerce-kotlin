package com.gymnastic.ecommerceapp.data.remote.dto.auth

/**
 * DTO para la petición de login
 *
 * Se envía al endpoint POST /auth/login
 */
data class LoginRequest(
    val email: String,
    val password: String
)
