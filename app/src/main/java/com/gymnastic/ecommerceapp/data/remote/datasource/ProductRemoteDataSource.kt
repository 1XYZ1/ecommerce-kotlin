package com.gymnastic.ecommerceapp.data.remote.datasource

import android.util.Log
import com.gymnastic.ecommerceapp.data.remote.api.ApiService
import com.gymnastic.ecommerceapp.data.remote.dto.product.CreateProductRequest
import com.gymnastic.ecommerceapp.data.remote.dto.product.ProductDto
import com.gymnastic.ecommerceapp.data.remote.dto.product.ProductListResponse
import com.gymnastic.ecommerceapp.domain.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Data Source para operaciones de productos con la API
 */
class ProductRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    /**
     * Obtiene lista de productos desde la API
     */
    suspend fun obtenerProductos(
        limite: Int = 100,
        busqueda: String? = null
    ): Result<ProductListResponse> {
        return try {
            Log.d("ProductRemoteDataSource", "Obteniendo productos de API, límite: $limite")
            val response = apiService.obtenerProductos(
                limite = limite,
                offset = 0,
                busqueda = busqueda
            )
            Log.d("ProductRemoteDataSource", "Productos recibidos: ${response.products.size}")
            Result.Exito(response)
        } catch (e: HttpException) {
            val error = "Error del servidor (${e.code()})"
            Log.e("ProductRemoteDataSource", error, e)
            Result.Error(error)
        } catch (e: IOException) {
            val error = "Error de conexión. Verifica tu internet"
            Log.e("ProductRemoteDataSource", error, e)
            Result.Error(error)
        } catch (e: Exception) {
            val error = "Error: ${e.message}"
            Log.e("ProductRemoteDataSource", error, e)
            Result.Error(error)
        }
    }

    /**
     * Obtiene un producto por ID
     */
    suspend fun obtenerProductoPorId(id: String): Result<ProductDto> {
        return try {
            val response = apiService.obtenerProductoPorId(id)
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                404 -> "Producto no encontrado"
                else -> "Error del servidor (${e.code()})"
            }
            Result.Error(mensaje)
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    /**
     * Crea un nuevo producto (solo admin)
     */
    suspend fun crearProducto(request: CreateProductRequest): Result<ProductDto> {
        return try {
            val response = apiService.crearProducto(request)
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "Datos inválidos"
                401 -> "No estás autenticado"
                403 -> "No tienes permisos de administrador"
                else -> "Error del servidor (${e.code()})"
            }
            Result.Error(mensaje)
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    /**
     * Actualiza un producto existente (solo admin)
     */
    suspend fun actualizarProducto(
        id: String,
        request: com.gymnastic.ecommerceapp.data.remote.dto.product.UpdateProductRequest
    ): Result<ProductDto> {
        return try {
            val response = apiService.actualizarProducto(id, request)
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "Datos inválidos"
                403 -> "No tienes permisos de administrador"
                404 -> "Producto no encontrado"
                else -> "Error del servidor (${e.code()})"
            }
            Result.Error(mensaje)
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    /**
     * Elimina un producto (solo admin)
     */
    suspend fun eliminarProducto(id: String): Result<Unit> {
        return try {
            apiService.eliminarProducto(id)
            Result.Exito(Unit)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                403 -> "No tienes permisos de administrador"
                404 -> "Producto no encontrado"
                else -> "Error del servidor (${e.code()})"
            }
            Result.Error(mensaje)
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
}
