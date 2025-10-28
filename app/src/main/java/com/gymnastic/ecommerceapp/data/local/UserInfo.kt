package com.gymnastic.ecommerceapp.data.local

/**
 * Clase de datos simple para representar la información del usuario
 *
 * Esta clase se usa para pasar información del usuario entre el ViewModel y la UI.
 * Es más simple que la entidad Usuario completa porque solo contiene los datos
 * que necesita mostrar la interfaz de usuario.
 *
 * @param nombre nombre del usuario
 * @param email email del usuario
 * @param estaLogueado estado de autenticación (true si está logueado)
 */
data class UserInfo(
    val nombre: String,
    val email: String,
    val estaLogueado: Boolean
)
