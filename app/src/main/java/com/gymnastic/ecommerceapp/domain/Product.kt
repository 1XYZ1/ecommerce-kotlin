package com.gymnastic.ecommerceapp.domain

/**
 * Modelo de dominio para Producto
 *
 * ACTUALIZADO: Incluye nuevos campos de la API
 * - slug: Identificador URL-friendly
 * - stock: Cantidad disponible
 * - sizes: Tallas disponibles
 * - type: Tipo de producto (alimento-seco, accesorios, etc.)
 * - species: Especie (cats, dogs, o null para universal)
 * - tags: Etiquetas para búsqueda
 * - images: Array de URLs de imágenes
 */
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String,
    val slug: String = "",
    val stock: Int = 0,
    val sizes: List<String> = emptyList(),
    val type: String = "",
    val species: String? = null,
    val tags: List<String> = emptyList(),
    val images: List<String> = emptyList()
)
