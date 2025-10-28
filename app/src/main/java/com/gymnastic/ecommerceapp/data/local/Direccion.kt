package com.gymnastic.ecommerceapp.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entidad para almacenar direcciones guardadas del usuario
 *
 * Permite al usuario guardar múltiples direcciones y establecer una como predeterminada.
 * Relacionada con la tabla de usuarios mediante foreign key.
 */
@Entity(
    tableName = "direcciones",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Direccion(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val usuarioId: String,
    val nombreCompleto: String,
    val telefono: String,
    val direccionCompleta: String,
    val esPredeterminada: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaUltimaActualizacion: Long = System.currentTimeMillis()
)
