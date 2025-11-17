package com.idnp2025b.solucionesmoviles.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "departamento",
    indices = [
        Index(value = ["nomDep"], unique = true)
    ]
)
data class Departamento (
    @PrimaryKey(autoGenerate = true)
    val codDep: Int = 0,        // Codigo del departamento
    val nomDep: String,         // Nombre del departamento
    val estDep: String          // Estado (A, I, E)
)
