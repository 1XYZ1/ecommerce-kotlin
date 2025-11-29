package com.gymnastic.ecommerceapp.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entidad para almacenar direcciones guardadas del usuario
 *
 * ACTUALIZADO: Eliminada foreign key a Usuario (ahora se usa API para auth)
 * usuarioId se mantiene como referencia pero sin constraint de base de datos
 */
@Entity(tableName = "direcciones")
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
