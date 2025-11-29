package com.gymnastic.ecommerceapp.domain

/**
 * Clase sellada para manejar resultados de operaciones con la API
 *
 * Esta clase representa tres estados posibles:
 * - Exito: La operación terminó bien y tenemos datos
 * - Error: La operación falló con un mensaje de error
 * - Cargando: La operación está en progreso
 *
 * CONCEPTOS PARA ESTUDIANTES:
 * - sealed class: Solo puede tener subclases definidas aquí
 * - Permite usar 'when' sin necesitar 'else'
 * - Patrón moderno para manejar estados async
 */
sealed class Result<out T> {
    data class Exito<T>(val datos: T) : Result<T>()
    data class Error(val mensaje: String) : Result<Nothing>()
    data object Cargando : Result<Nothing>()
}
