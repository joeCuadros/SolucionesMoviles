package com.idnp2025b.solucionesmoviles.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plantas")
data class Planta(
    @PrimaryKey(autoGenerate = true)
    val codPla: Int = 0,        // Codigo de planta
    val nomPla: String,         // Nombre de planta
    val estPla: String            // Estado (A, I, E)
)