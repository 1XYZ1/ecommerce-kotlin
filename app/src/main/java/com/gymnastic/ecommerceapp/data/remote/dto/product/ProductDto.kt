package com.gymnastic.ecommerceapp.data.remote.dto.product

import com.gymnastic.ecommerceapp.domain.Product
import com.google.gson.*
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Deserializador custom para images que soporta tanto strings como objetos
 * También convierte URLs de localhost a producción y construye URLs completas
 */
class ImagesDeserializer : JsonDeserializer<List<String>> {
    companion object {
        private const val BASE_URL = "https://pet-shop-back-production.up.railway.app/api/files/product/"
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<String> {
        android.util.Log.d("ImagesDeserializer", "Deserializando imágenes, tipo: ${json.javaClass.simpleName}")

        if (!json.isJsonArray) {
            android.util.Log.w("ImagesDeserializer", "JSON no es un array, devolviendo lista vacía")
            return emptyList()
        }

        val array = json.asJsonArray
        android.util.Log.d("ImagesDeserializer", "Array de imágenes tiene ${array.size()} elementos")

        val resultado = array.mapNotNull { element ->
            val url = when {
                element.isJsonPrimitive && element.asJsonPrimitive.isString -> {
                    val urlString = element.asString
                    android.util.Log.d("ImagesDeserializer", "Imagen encontrada (string): $urlString")
                    urlString
                }
                element.isJsonObject -> {
                    val urlString = element.asJsonObject.get("url")?.asString
                    android.util.Log.d("ImagesDeserializer", "Imagen encontrada (objeto): $urlString")
                    urlString
                }
                else -> {
                    android.util.Log.w("ImagesDeserializer", "Elemento desconocido: ${element.javaClass.simpleName}")
                    null
                }
            }

            if (url == null) return@mapNotNull null

            // Construir URL completa
            val urlFinal = when {
                // Si ya es una URL completa (empieza con http), solo convertir localhost a producción
                url.startsWith("http://localhost:3000/api") -> {
                    url.replace("http://localhost:3000/api", "https://pet-shop-back-production.up.railway.app/api")
                }
                url.startsWith("http") -> {
                    url // Ya es una URL completa de producción
                }
                // Si es solo un nombre de archivo, construir la URL completa
                else -> {
                    "$BASE_URL$url"
                }
            }

            android.util.Log.d("ImagesDeserializer", "URL final: $urlFinal")
            urlFinal
        }

        android.util.Log.d("ImagesDeserializer", "Total de imágenes procesadas: ${resultado.size}")
        return resultado
    }
}

/**
 * DTO para producto individual de la API
 */
data class ProductDto(
    val id: String,
    val title: String,
    val price: Double,
    val description: String?,
    val slug: String,
    val stock: Int,
    val sizes: List<String>,
    val type: String,
    val species: String?,
    val tags: List<String>,
    @JsonAdapter(ImagesDeserializer::class)
    val images: List<String> = emptyList()
)

/**
 * Response de lista de productos
 */
data class ProductListResponse(
    val products: List<ProductDto>,
    val total: Int
)

/**
 * Request para crear producto
 */
data class CreateProductRequest(
    val title: String,
    val price: Double,
    val description: String,
    val stock: Int,
    val sizes: List<String> = listOf("UNICO"),
    val type: String = "accesorios",
    val species: String? = null,
    val tags: List<String> = emptyList(),
    val images: List<String> = emptyList()
)

/**
 * Request para actualizar producto
 * Solo incluye los campos editables (title, description, price, stock)
 * NO incluye images, sizes, type, etc. para no sobrescribirlos
 */
data class UpdateProductRequest(
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int
)

/**
 * Convierte ProductDto a Product (modelo de dominio)
 */
fun ProductDto.toDomain(): Product {
    val firstImage = images.firstOrNull() ?: ""
    android.util.Log.d("ProductDto", "Convirtiendo producto: id=$id, title=$title")
    android.util.Log.d("ProductDto", "  Images array: ${images.size} elementos")
    android.util.Log.d("ProductDto", "  Primera imagen: $firstImage")

    return Product(
        id = id,
        name = title,
        price = price,
        description = description ?: "",
        imageUrl = firstImage,
        slug = slug,
        stock = stock,
        sizes = sizes,
        type = type,
        species = species,
        tags = tags,
        images = images
    )
}
