package com.idnp2025b.solucionesmoviles.repository

import com.idnp2025b.solucionesmoviles.data.dao.DepartamentoDao
import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.dao.TipoTallerDao
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val plantaDao: PlantaDao,
    private val departamentoDao: DepartamentoDao,
    private val tipoTallerDao: TipoTallerDao
){
    // --- METODOS DE PLANTA ---
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

    suspend fun eliminarLogicoPlanta(codPla: Int) {
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
    // --- METODOS DE DEPARTAMENTO ---
    suspend fun insertarDepartamento(departamento: Departamento) {
        departamentoDao.insertarDepartamento(departamento)
    }

    suspend fun updateDepartamento(departamento: Departamento) {
        departamentoDao.updateDepartamento(departamento)
    }

    suspend fun activarDepartamento(codDep: Int) {
        departamentoDao.activarDepartamento(codDep)
    }

    suspend fun inactivarDepartamento(codDep: Int) {
        departamentoDao.inactivarDepartamento(codDep)
    }

    suspend fun deleteDepartamento(departamento: Departamento) {
        departamentoDao.deleteDepartamento(departamento)
    }

    suspend fun eliminarLogicoDepartamento(codDep: Int) {
        departamentoDao.eliminarLogicoDepartamento(codDep = codDep)
    }

    fun getDepartamentoTodas(): Flow<List<Departamento>> {
        return departamentoDao.getDepartamentoTodas()
    }

    fun getDepartamentoActivas(): Flow<List<Departamento>> {
        return departamentoDao.getDepartamentoActivas()
    }

    fun getDepartamentoInactivas(): Flow<List<Departamento>> {
        return departamentoDao.getDepartamentoInactivas()
    }

    fun getDepartamentoEliminadas(): Flow<List<Departamento>> {
        return departamentoDao.getDepartamentoEliminadas()
    }

    fun getDepartamento(codDep: Int): Flow<Departamento?> {
        return departamentoDao.getDepartamento(codDep)
    }
    // --- MÉTODOS DE TIPO DE TALLER ---

    suspend fun insertarTipoTaller(tipoTaller: TipoTaller) {
        // Asumiendo que arreglaste el DAO (param: tipoTaller)
        tipoTallerDao.insertarTipoTaller(tipoTaller)
    }
    suspend fun updateTipoTaller(tipoTaller: TipoTaller) {
        // Asumiendo que arreglaste el DAO (param: tipoTaller)
        tipoTallerDao.updateTipoTaller(tipoTaller)
    }
    suspend fun activarTipoTaller(codTipTal: Int) {
        tipoTallerDao.activarTipoTaller(codTipTal)
    }
    suspend fun inactivarTipoTaller(codTipTal: Int) {
        tipoTallerDao.inactivarTipoTaller(codTipTal)
    }
    suspend fun deleteTipoTaller(tipoTaller: TipoTaller) {
        // Asumiendo que arreglaste el DAO (param: tipoTaller)
        tipoTallerDao.deleteTipoTaller(tipoTaller)
    }
    suspend fun eliminarLogicoTipoTaller(codTipTal: Int) {
        tipoTallerDao.eliminarLogicoTipoTaller(codTipTal)
    }
    fun getTipoTallersTodas(): Flow<List<TipoTaller>> {
        return tipoTallerDao.getTipoTallersTodas()
    }
    fun getTipoTallersActivas(): Flow<List<TipoTaller>> {
        return tipoTallerDao.getTipoTallersActivas()
    }
    fun getTipoTallersInactivas(): Flow<List<TipoTaller>> {
        return tipoTallerDao.getTipoTallersInactivas()
    }
    fun getTipoTallersEliminadas(): Flow<List<TipoTaller>> {
        return tipoTallerDao.getTipoTallersEliminadas()
    }
    fun getTipoTaller(codTipTal: Int): Flow<TipoTaller?> {
        return tipoTallerDao.getTipoTaller(codTipTal)
    }
}