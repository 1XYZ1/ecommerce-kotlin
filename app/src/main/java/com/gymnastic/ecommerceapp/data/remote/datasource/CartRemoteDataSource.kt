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
            android.util.Log.d("CartRemoteDataSource", "========== AGREGANDO ITEM AL CARRITO ==========")
            android.util.Log.d("CartRemoteDataSource", "Request:")
            android.util.Log.d("CartRemoteDataSource", "  productId: $productId")
            android.util.Log.d("CartRemoteDataSource", "  quantity: $quantity")
            android.util.Log.d("CartRemoteDataSource", "  size: $size")

            val request = AddCartItemRequest(productId, quantity, size)
            val response = apiService.agregarAlCarrito(request)

            android.util.Log.d("CartRemoteDataSource", "Respuesta de API recibida:")
            android.util.Log.d("CartRemoteDataSource", "  Cart ID: ${response.id}")
            android.util.Log.d("CartRemoteDataSource", "  User ID: ${response.userId}")
            android.util.Log.d("CartRemoteDataSource", "  Items count: ${response.items.size}")
            android.util.Log.d("CartRemoteDataSource", "  Subtotal: ${response.subtotal}")
            android.util.Log.d("CartRemoteDataSource", "  Tax: ${response.tax}")
            android.util.Log.d("CartRemoteDataSource", "  Total: ${response.total}")

            response.items.forEachIndexed { index, item ->
                android.util.Log.d("CartRemoteDataSource", "  Item $index:")
                android.util.Log.d("CartRemoteDataSource", "    id: ${item.id}")
                android.util.Log.d("CartRemoteDataSource", "    productId: ${item.productId}")
                android.util.Log.d("CartRemoteDataSource", "    quantity: ${item.quantity}")
                android.util.Log.d("CartRemoteDataSource", "    size: ${item.size}")
                android.util.Log.d("CartRemoteDataSource", "    priceAtTime: ${item.priceAtTime}")
                android.util.Log.d("CartRemoteDataSource", "    product: ${if (item.product != null) "present" else "NULL"}")
            }

            android.util.Log.d("CartRemoteDataSource", "========== FIN AGREGAR ITEM ==========")
            Result.Exito(response)
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                400 -> "Producto no disponible o stock insuficiente"
                401 -> "No estás autenticado"
                404 -> "Producto no encontrado"
                else -> "Error del servidor (${e.code()})"
            }
            android.util.Log.e("CartRemoteDataSource", "Error HTTP: ${e.code()} - $mensaje", e)
            Result.Error(mensaje)
        } catch (e: IOException) {
            android.util.Log.e("CartRemoteDataSource", "Error de conexión", e)
            Result.Error("Error de conexión")
        } catch (e: Exception) {
            android.util.Log.e("CartRemoteDataSource", "Error inesperado", e)
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
