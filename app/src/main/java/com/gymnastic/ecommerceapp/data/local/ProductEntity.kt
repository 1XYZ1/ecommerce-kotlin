package com.gymnastic.ecommerceapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gymnastic.ecommerceapp.domain.Product

/**
 * Entidad de Room para cachear productos de la API
 *
 * Esta tabla guarda los productos localmente para:
 * - Mejorar velocidad de carga
 * - Mostrar productos si no hay conexión
 * - Reducir llamadas a la API
 */
@Entity(tableName = "products_cache")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val price: Double,
    val description: String,
    val imageUrl: String,
    val slug: String,
    val stock: Int,
    val sizes: String, // Guardado como JSON o separado por comas
    val type: String,
    val species: String,
    val tags: String, // Separado por comas
    val images: String, // URLs separadas por comas
    val cacheTimestamp: Long = System.currentTimeMillis()
)

/**
 * Convierte ProductEntity a Product (modelo de dominio)
 */
fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = title,
        price = price,
        description = description,
        imageUrl = imageUrl,
        slug = slug,
        stock = stock,
        sizes = if (sizes.isBlank()) emptyList() else sizes.split(","),
        type = type,
        species = species.takeIf { it.isNotBlank() },
        tags = if (tags.isBlank()) emptyList() else tags.split(","),
        images = if (images.isBlank()) emptyList() else images.split(",")
    )
}

/**
 * Convierte Product (dominio) a ProductEntity para guardar en Room
 */
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = name,
        price = price,
        description = description,
        imageUrl = imageUrl,
        slug = slug,
        stock = stock,
        sizes = sizes.joinToString(","),
        type = type,
        species = species ?: "",
        tags = tags.joinToString(","),
        images = images.joinToString(",")
    )
}
