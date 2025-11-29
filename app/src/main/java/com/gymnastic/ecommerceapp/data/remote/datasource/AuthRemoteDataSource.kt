package com.gymnastic.ecommerceapp.data.remote.datasource

import android.util.Log
import com.gymnastic.ecommerceapp.data.remote.api.ApiService
import com.gymnastic.ecommerceapp.data.remote.dto.auth.AuthResponse
import com.gymnastic.ecommerceapp.data.remote.dto.auth.LoginRequest
import com.gymnastic.ecommerceapp.data.remote.dto.auth.RegisterRequest
import com.gymnastic.ecommerceapp.domain.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Data Source para operaciones de autenticación con la API
 *
 * Maneja las llamadas HTTP relacionadas con auth y convierte
 * las excepciones en objetos Result con mensajes amigables
 *
 * CONCEPTOS PARA ESTUDIANTES:
 * - try-catch: Captura errores de red
 * - HttpException: Errores HTTP (400, 401, 500, etc.)
 * - IOException: Errores de conexión (sin internet, timeout)
 * - Result: Patrón para manejar éxito/error de forma segura
 */
class AuthRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    /**
     * Inicia sesión con email y contraseña
     *
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result.Exito con AuthResponse si funciona, Result.Error si falla
     */
    suspend fun iniciarSesion(email: String, password: String): Result<AuthResponse> {
        return try {
            Log.d("AuthRemoteDataSource", "Intentando login para: $email")
            val request = LoginRequest(email, password)
            val response = apiService.iniciarSesion(request)

            Log.d("AuthRemoteDataSource", "========== RESPUESTA DE API ==========")
            Log.d("AuthRemoteDataSource", "Token: ${response.token?.take(20)}...")
            Log.d("AuthRemoteDataSource", "User.ID: ${response.user?.id}")
            Log.d("AuthRemoteDataSource", "User.fullName: ${response.user?.fullName}")
            Log.d("AuthRemoteDataSource", "User.Email: ${response.user?.email}")
            Log.d("AuthRemoteDataSource", "User.isActive: ${response.user?.isActive}")
            Log.d("AuthRemoteDataSource", "User.Roles: ${response.user?.roles}")
            Log.d("AuthRemoteDataSource", "====================================")

            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "Datos inválidos"
                401 -> "Email o contraseña incorrectos"
                429 -> "Demasiados intentos. Intenta más tarde"
                500 -> "Error del servidor. Intenta más tarde"
                else -> "Error del servidor (${e.code()})"
            }
            Log.e("AuthRemoteDataSource", "Error HTTP ${e.code()}: $mensaje", e)
            Result.Error(mensaje)
        } catch (e: IOException) {
            Log.e("AuthRemoteDataSource", "Error de conexión", e)
            Result.Error("Error de conexión. Verifica tu internet")
        } catch (e: Exception) {
            Log.e("AuthRemoteDataSource", "Error inesperado", e)
            Result.Error("Error inesperado: ${e.message}")
        }
    }

    /**
     * Registra un nuevo usuario
     *
     * @param nombre Nombre completo del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result.Exito con AuthResponse si funciona, Result.Error si falla
     */
    suspend fun registrar(
        nombre: String,
        email: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(
                fullName = nombre,
                email = email,
                password = password
            )
            val response = apiService.registrar(request)
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "El email ya está registrado o los datos son inválidos"
                429 -> "Demasiados intentos. Intenta más tarde"
                500 -> "Error del servidor. Intenta más tarde"
                else -> "Error del servidor (${e.code()})"
            }
            Result.Error(mensaje)
        } catch (e: IOException) {
            Result.Error("Error de conexión. Verifica tu internet")
        } catch (e: Exception) {
            Result.Error("Error inesperado: ${e.message}")
        }
    }

    /**
     * Verifica el estado de autenticación
     *
     * Llama a /auth/check-status para validar el token actual
     *
     * @return Result.Exito si el token es válido, Result.Error si no
     */
    suspend fun verificarEstado(): Result<AuthResponse> {
        return try {
            val response = apiService.verificarEstado()
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                401 -> "Sesión expirada. Por favor inicia sesión"
                403 -> "Token inválido"
                else -> "Error del servidor (${e.code()})"
            }
            Result.Error(mensaje)
        } catch (e: IOException) {
            Result.Error("Error de conexión. Verifica tu internet")
        } catch (e: Exception) {
            Result.Error("Error inesperado: ${e.message}")
        }
    }
}
