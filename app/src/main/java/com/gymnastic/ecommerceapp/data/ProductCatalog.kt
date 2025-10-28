package com.gymnastic.ecommerceapp.data

import com.gymnastic.ecommerceapp.domain.Product

object ProductCatalog {
    val products = listOf(
        Product(
            id = "1",
            name = "Smartphone Galaxy",
            price = 299.99,
            description = "Smartphone Android con pantalla de 6.1 pulgadas y cámara de 48MP",
            imageUrl = "file:///android_asset/images/test1.webp"
        ),
        Product(
            id = "2",
            name = "Laptop Pro",
            price = 1299.99,
            description = "Laptop profesional con procesador Intel i7 y 16GB RAM",
            imageUrl = "file:///android_asset/images/test2.webp"
        ),
        Product(
            id = "3",
            name = "Auriculares Wireless",
            price = 89.99,
            description = "Auriculares inalámbricos con cancelación de ruido",
            imageUrl = "file:///android_asset/images/test3.webp"
        ),
        Product(
            id = "4",
            name = "Smart Watch",
            price = 199.99,
            description = "Reloj inteligente con GPS y monitor de frecuencia cardíaca",
            imageUrl = "file:///android_asset/images/test4.webp"
        ),
        Product(
            id = "5",
            name = "Tablet 10\"",
            price = 399.99,
            description = "Tablet Android con pantalla HD de 10 pulgadas",
            imageUrl = "file:///android_asset/images/test5.webp"
        )
    )
}
