package com.gymnastic.ecommerceapp.data.local

/**
 * Clase de datos simple para representar la información del usuario
 *
 * ACTUALIZADO: Ahora incluye campo esAdmin para funcionalidad de administrador
 *
 * @param nombre nombre del usuario
 * @param email email del usuario
 * @param estaLogueado estado de autenticación (true si está logueado)
 * @param esAdmin si el usuario tiene privilegios de administrador
 */
data class UserInfo(
    val nombre: String,
    val email: String,
    val estaLogueado: Boolean,
    val esAdmin: Boolean = false
)
