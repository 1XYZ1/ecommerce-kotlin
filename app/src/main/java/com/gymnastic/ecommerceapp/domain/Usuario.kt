package com.gymnastic.ecommerceapp.domain

/**
 * Modelo de dominio para Usuario
 *
 * Representa un usuario en la aplicación.
 * Este es el modelo que usa la UI, no la base de datos.
 *
 * CONCEPTOS PARA ESTUDIANTES:
 * - Domain Model: Modelo "puro" sin dependencias de Android o Room
 * - Se usa en ViewModels y UI
 * - Se convierte desde/hacia DTOs de la API
 */
data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val esAdmin: Boolean = false,
    val estaActivo: Boolean = true
)
