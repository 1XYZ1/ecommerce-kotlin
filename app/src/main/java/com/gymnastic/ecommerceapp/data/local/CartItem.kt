package com.gymnastic.ecommerceapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String,
    val quantity: Int
)
