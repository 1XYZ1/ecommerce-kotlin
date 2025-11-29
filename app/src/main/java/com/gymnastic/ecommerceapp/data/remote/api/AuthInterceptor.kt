package com.gymnastic.ecommerceapp.data.remote.api

import com.gymnastic.ecommerceapp.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor de OkHttp que agrega automáticamente el token JWT
 * a todas las peticiones que requieren autenticación
 *
 * CÓMO FUNCIONA:
 * 1. Antes de cada petición HTTP, este interceptor se ejecuta
 * 2. Obtiene el token guardado del TokenManager
 * 3. Si existe token, lo agrega al header "Authorization: Bearer {token}"
 * 4. Si la respuesta es 401 (no autorizado), elimina el token
 *
 * CONCEPTOS PARA ESTUDIANTES:
 * - Interceptor: Se ejecuta automáticamente en cada petición HTTP
 * - runBlocking: Necesario porque Interceptor es síncrono pero TokenManager es suspend
 * - Bearer Token: Estándar para enviar JWT en headers HTTP
 */
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestOriginal = chain.request()

        // Obtener el token de forma síncrona
        // (runBlocking es necesario porque intercept() no es suspend)
        val token = runBlocking {
            tokenManager.obtenerToken()
        }

        // Si no hay token, continuar sin modificar el request
        if (token.isNullOrEmpty()) {
            return chain.proceed(requestOriginal)
        }

        // Agregar el header Authorization con el token
        val requestConToken = requestOriginal.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        // Ejecutar la petición
        val response = chain.proceed(requestConToken)

        // Si el servidor responde 401 (no autorizado), el token expiró
        // Lo eliminamos para que el usuario tenga que hacer login de nuevo
        if (response.code == 401) {
            runBlocking {
                tokenManager.eliminarToken()
            }
        }

        return response
    }
}
