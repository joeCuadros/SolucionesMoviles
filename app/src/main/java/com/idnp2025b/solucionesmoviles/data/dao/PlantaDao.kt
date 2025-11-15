package com.idnp2025b.solucionesmoviles.data.dao

import androidx.room.*
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import kotlinx.coroutines.flow.Flow

@Dao
interface  PlantaDao {

    @Insert
    suspend fun insertarPlanta(planta: Planta)

    // Actualizar
    @Update
    suspend fun updatePlanta(planta: Planta)
    @Query("UPDATE plantas SET estPla = 'A' WHERE codPla = :codPla")
    suspend fun activarPlanta(codPla: Int)
    @Query("UPDATE plantas SET estPla = 'I' WHERE codPla = :codPla")
    suspend fun inactivarPlanta(codPla: Int)


    // Eliminaciones
    @Delete
    suspend fun deletePlanta(planta: Planta)
    @Query("UPDATE plantas SET estPla = 'E' WHERE codPla = :codPla")
    suspend fun eliminarLogico(codPla: Int)

    // Obtener todas
    @Query("SELECT * FROM plantas")
    fun getPlantasTodas(): Flow<List<Planta>>

    @Query("SELECT * FROM plantas WHERE estPla = 'A'")
    fun getPlantasActivas(): Flow<List<Planta>>

    @Query("SELECT * FROM plantas WHERE estPla = 'I'")
    fun getPlantasInactivas(): Flow<List<Planta>>

    @Query("SELECT * FROM plantas WHERE estPla = 'E'")
    fun getPlantasEliminadas(): Flow<List<Planta>>

    @Query("SELECT * FROM plantas WHERE codPla = :codPla")
    suspend fun getPlanta(codPla: Int): Planta?
}