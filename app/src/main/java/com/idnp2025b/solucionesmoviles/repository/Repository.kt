package com.idnp2025b.solucionesmoviles.repository

import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val plantaDao: PlantaDao
){
    suspend fun insertarPlanta(planta: Planta) {
        plantaDao.insertarPlanta(planta)
    }

    // Actualizar
    suspend fun updatePlanta(planta: Planta) {
        plantaDao.updatePlanta(planta)
    }

    suspend fun activarPlanta(codPla: Int) {
        plantaDao.activarPlanta(codPla)
    }

    suspend fun inactivarPlanta(codPla: Int) {
        plantaDao.inactivarPlanta(codPla)
    }

    // Eliminaciones
    suspend fun deletePlanta(planta: Planta) {
        plantaDao.deletePlanta(planta)
    }

    suspend fun eliminarLogico(codPla: Int) {
        plantaDao.eliminarLogico(codPla)
    }

    // Obtener todas
    fun getPlantasTodas(): Flow<List<Planta>> {
        return plantaDao.getPlantasTodas()
    }

    fun getPlantasActivas(): Flow<List<Planta>> {
        return plantaDao.getPlantasActivas()
    }

    fun getPlantasInactivas(): Flow<List<Planta>> {
        return plantaDao.getPlantasInactivas()
    }

    fun getPlantasEliminadas(): Flow<List<Planta>> {
        return plantaDao.getPlantasEliminadas()
    }

    fun getPlanta(codPla: Int): Flow<Planta?> {
        return plantaDao.getPlanta(codPla)
    }
}