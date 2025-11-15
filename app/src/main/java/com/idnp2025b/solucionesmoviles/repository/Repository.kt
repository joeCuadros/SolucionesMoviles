package com.idnp2025b.solucionesmoviles.repository

import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val plantaDao: PlantaDao
){
    fun getPlantasTodas(): Flow<List<Planta>> = plantaDao.getPlantasTodas()
    suspend fun insertarPlanta(planta: Planta) = plantaDao.insertarPlanta(planta)
}