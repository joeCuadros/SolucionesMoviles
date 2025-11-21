package com.idnp2025b.solucionesmoviles.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TallerConDetalles (
    @Embedded
    val taller: Taller,
    @Relation(
        parentColumn = "codPla",    // Columna en 'Taller' (FK)
        entityColumn = "codPla"     // Columna en 'Planta' (PK)
    )
    val planta: Planta?,
    @Relation(
        parentColumn = "codDep",
        entityColumn = "codDep"
    )
    val departamento: Departamento?,
    @Relation(
        parentColumn = "codTipTal",
        entityColumn = "codTipTal"
    )
    val tipoTaller: TipoTaller
)