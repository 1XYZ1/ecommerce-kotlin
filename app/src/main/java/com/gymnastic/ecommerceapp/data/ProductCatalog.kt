package com.gymnastic.ecommerceapp.data

import com.gymnastic.ecommerceapp.domain.Product

object ProductCatalog {
    val products = listOf(
        Product(
            id = "1",
            name = "Smartphone Galaxy",
            price = 299.99,
            description = "Smartphone Android con pantalla de 6.1 pulgadas y cámara de 48MP",
            imageUrl = "https://via.placeholder.com/300x300/4CAF50/FFFFFF?text=Galaxy"
        ),
        Product(
            id = "2",
            name = "Laptop Pro",
            price = 1299.99,
            description = "Laptop profesional con procesador Intel i7 y 16GB RAM",
            imageUrl = "https://via.placeholder.com/300x300/2196F3/FFFFFF?text=Laptop"
        ),
        Product(
            id = "3",
            name = "Auriculares Wireless",
            price = 89.99,
            description = "Auriculares inalámbricos con cancelación de ruido",
            imageUrl = "https://via.placeholder.com/300x300/FF9800/FFFFFF?text=Headphones"
        ),
        Product(
            id = "4",
            name = "Smart Watch",
            price = 199.99,
            description = "Reloj inteligente con GPS y monitor de frecuencia cardíaca",
            imageUrl = "https://via.placeholder.com/300x300/E91E63/FFFFFF?text=Watch"
        ),
        Product(
            id = "5",
            name = "Tablet 10\"",
            price = 399.99,
            description = "Tablet Android con pantalla HD de 10 pulgadas",
            imageUrl = "https://via.placeholder.com/300x300/9C27B0/FFFFFF?text=Tablet"
        )
    )
}
