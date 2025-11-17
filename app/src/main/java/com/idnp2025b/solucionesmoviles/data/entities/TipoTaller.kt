package com.idnp2025b.solucionesmoviles.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tipo_taller",
    indices = [
        Index(value = ["nomTipTal"], unique = true)
    ]
)
data class TipoTaller (
    @PrimaryKey(autoGenerate = true)
    val codTipTal: Int = 0,        // Codigo de tipo de taller
    val nomTipTal: String,         // Nombre de tipo de taller
    val estTipTal: String          // Estado (A, I, E)
)