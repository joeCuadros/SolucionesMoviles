package com.idnp2025b.solucionesmoviles.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idnp2025b.solucionesmoviles.data.dao.DepartamentoDao
import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.dao.TipoTallerDao
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller

@Database(
    entities = [
        Planta::class,
        Departamento::class,
        TipoTaller::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun plantaDao(): PlantaDao
    abstract fun departamentoDao(): DepartamentoDao
    abstract fun tipoTallerDao(): TipoTallerDao
}