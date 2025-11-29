package com.gymnastic.ecommerceapp.data.remote.datasource

import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.data.remote.api.ApiService
import com.gymnastic.ecommerceapp.data.remote.dto.cart.*
import com.gymnastic.ecommerceapp.domain.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Data Source para operaciones de carrito con la API
 */
class CartRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    /**
     * Obtiene el carrito del usuario desde la API
     */
    suspend fun obtenerCarrito(): Result<CartDto> {
        return try {
            val response = apiService.obtenerCarrito()
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                401 -> "No estás autenticado"
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
     * Agrega un item al carrito
     */
    suspend fun agregarItem(
        productId: String,
        quantity: Int,
        size: String = "UNICO"
    ): Result<CartDto> {
        return try {
            val request = AddCartItemRequest(productId, quantity, size)
            val response = apiService.agregarAlCarrito(request)
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "Producto no disponible o stock insuficiente"
                401 -> "No estás autenticado"
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
     * Actualiza la cantidad de un item
     */
    suspend fun actualizarCantidad(
        itemId: String,
        quantity: Int
    ): Result<CartDto> {
        return try {
            val request = UpdateCartItemRequest(quantity)
            val response = apiService.actualizarCantidad(itemId, request)
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "Stock insuficiente"
                404 -> "Item no encontrado"
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
     * Elimina un item del carrito
     */
    suspend fun eliminarItem(itemId: String): Result<CartDto> {
        return try {
            val response = apiService.eliminarDelCarrito(itemId)
            Result.Exito(response)
        } catch (e: HttpException) {
            Result.Error("Error del servidor (${e.code()})")
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    /**
     * Vacía el carrito completamente
     */
    suspend fun vaciarCarrito(): Result<CartDto> {
        return try {
            val response = apiService.vaciarCarrito()
            Result.Exito(response)
        } catch (e: HttpException) {
            Result.Error("Error del servidor (${e.code()})")
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    /**
     * Sincroniza items de carrito local con el servidor
     */
    suspend fun sincronizarCarrito(itemsLocales: List<CartItem>): Result<SyncCartResponse> {
        return try {
            val syncItems = itemsLocales.map {
                SyncCartItem(
                    productId = it.productId,
                    quantity = it.quantity
                )
            }
            val request = SyncCartRequest(syncItems)
            val response = apiService.sincronizarCarrito(request)
            Result.Exito(response)
        } catch (e: HttpException) {
            Result.Error("Error al sincronizar carrito (${e.code()})")
        } catch (e: IOException) {
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
}
