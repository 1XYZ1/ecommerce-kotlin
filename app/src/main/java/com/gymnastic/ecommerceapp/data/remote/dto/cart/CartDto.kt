package com.gymnastic.ecommerceapp.data.remote.dto.cart

import com.gymnastic.ecommerceapp.data.remote.dto.product.ProductDto

/**
 * DTOs para operaciones de carrito
 */

/**
 * Item del carrito desde la API
 */
data class CartItemDto(
    val id: String,
    val cartId: String,
    val productId: String,
    val quantity: Int,
    val size: String,
    val priceAtTime: Double,
    val product: ProductDto
)

/**
 * Carrito completo desde la API
 */
data class CartDto(
    val id: String,
    val userId: String,
    val items: List<CartItemDto>,
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val updatedAt: String,
    val createdAt: String
)

/**
 * Request para agregar item al carrito
 */
data class AddCartItemRequest(
    val productId: String,
    val quantity: Int,
    val size: String = "UNICO"
)

/**
 * Request para actualizar cantidad de item
 */
data class UpdateCartItemRequest(
    val quantity: Int
)

/**
 * Request para sincronizar carrito de invitado
 */
data class SyncCartRequest(
    val items: List<SyncCartItem>
)

data class SyncCartItem(
    val productId: String,
    val quantity: Int,
    val size: String = "UNICO"
)

/**
 * Response de sincronización de carrito
 */
data class SyncCartResponse(
    val synced: Int,
    val failed: List<FailedSyncItem>,
    val cart: CartDto
)

data class FailedSyncItem(
    val item: SyncCartItem,
    val reason: String
)

/**
 * Convierte CartItemDto (API) a CartItem (Room)
 */
fun CartItemDto.toLocal(): com.gymnastic.ecommerceapp.data.local.CartItem {
    return com.gymnastic.ecommerceapp.data.local.CartItem(
        productId = productId,
        productName = product.title,
        productPrice = product.price,
        productImageUrl = product.images.firstOrNull() ?: "",
        quantity = quantity
    )
}
