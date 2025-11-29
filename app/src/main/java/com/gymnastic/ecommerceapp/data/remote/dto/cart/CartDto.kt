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
    val product: ProductDto? = null
)

/**
 * Carrito completo desde la API
 */
data class CartDto(
    val id: String,
    val userId: String,
    val items: List<CartItemDto> = emptyList(),
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val updatedAt: String? = null,
    val createdAt: String? = null
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
 *
 * Maneja el caso donde product puede ser null, usando priceAtTime como fallback
 */
fun CartItemDto.toLocal(): com.gymnastic.ecommerceapp.data.local.CartItem {
    android.util.Log.d("CartDto", "========== CONVIRTIENDO CartItemDto a CartItem ==========")
    android.util.Log.d("CartDto", "CartItemDto recibido:")
    android.util.Log.d("CartDto", "  id: $id")
    android.util.Log.d("CartDto", "  productId: $productId")
    android.util.Log.d("CartDto", "  quantity: $quantity")
    android.util.Log.d("CartDto", "  size: $size")
    android.util.Log.d("CartDto", "  priceAtTime: $priceAtTime")
    android.util.Log.d("CartDto", "  product: $product")

    if (product != null) {
        android.util.Log.d("CartDto", "  product.title: ${product.title}")
        android.util.Log.d("CartDto", "  product.price: ${product.price}")
        android.util.Log.d("CartDto", "  product.images: ${product.images.size} imágenes")
    } else {
        android.util.Log.w("CartDto", "  ⚠️ product es NULL")
    }

    val cartItem = com.gymnastic.ecommerceapp.data.local.CartItem(
        productId = productId,
        productName = product?.title ?: "Producto",
        productPrice = product?.price ?: priceAtTime,
        productImageUrl = product?.images?.firstOrNull() ?: "",
        quantity = quantity
    )

    android.util.Log.d("CartDto", "CartItem creado:")
    android.util.Log.d("CartDto", "  productId: ${cartItem.productId}")
    android.util.Log.d("CartDto", "  productName: ${cartItem.productName}")
    android.util.Log.d("CartDto", "  productPrice: ${cartItem.productPrice}")
    android.util.Log.d("CartDto", "  productImageUrl: ${cartItem.productImageUrl}")
    android.util.Log.d("CartDto", "  quantity: ${cartItem.quantity}")
    android.util.Log.d("CartDto", "========== FIN CONVERSIÓN ==========")

    return cartItem
}
