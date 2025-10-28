package com.gymnastic.ecommerceapp.domain

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String
)
