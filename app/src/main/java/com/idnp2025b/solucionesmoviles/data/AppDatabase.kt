package com.idnp2025b.solucionesmoviles.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.entities.Planta

@Database(
    entities = [
        Planta::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun plantaDao(): PlantaDao
}