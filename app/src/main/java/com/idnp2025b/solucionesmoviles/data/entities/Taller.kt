package com.idnp2025b.solucionesmoviles.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "taller",
    foreignKeys = [
        ForeignKey(
            entity = TipoTaller::class,
            parentColumns = ["codTipTal"],
            childColumns = ["codTipTal"],
            onDelete = ForeignKey.Companion.RESTRICT,
            onUpdate = ForeignKey.Companion.CASCADE,
        ),
        ForeignKey(
            entity = Departamento::class,
            parentColumns = ["codDep"],
            childColumns = ["codDep"],
            onDelete = ForeignKey.Companion.RESTRICT,
            onUpdate = ForeignKey.Companion.CASCADE,
        ),
        ForeignKey(
            entity = Planta::class,
            parentColumns = ["codPla"],
            childColumns = ["codPla"],
            onDelete = ForeignKey.Companion.RESTRICT,
            onUpdate = ForeignKey.Companion.CASCADE,
        )
    ],
    indices = [
        Index(value = ["codTipTal"]),
        Index(value = ["codDep"]),
        Index(value = ["codPla"]),
        Index(value = ["nomTal"], unique = true)
    ]
)
data class Taller (
    @PrimaryKey(autoGenerate = true)
    val codTal: Int = 0,        // Codigo del taller
    val nomTal: String,         // Nombre del taller
    val codTipTal: Int,         // (FK) Codigo de tipo de taller
    val codDep: Int,            // (FK) Codigo de departamento
    val codPla: Int,            // (FK) Codigo de planta
    val estTal: String          // Estado (A, I, E)
)