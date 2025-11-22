package com.idnp2025b.solucionesmoviles.repository

import androidx.room.withTransaction
import com.idnp2025b.solucionesmoviles.data.AppDatabase
import com.idnp2025b.solucionesmoviles.data.dao.DepartamentoDao
import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.dao.TallerDao
import com.idnp2025b.solucionesmoviles.data.dao.TipoTallerDao
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.data.entities.Taller
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val db: AppDatabase,
    private val plantaDao: PlantaDao,
    private val departamentoDao: DepartamentoDao,
    private val tipoTallerDao: TipoTallerDao,
    private val tallerDao: TallerDao
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
    // --- MÉTODOS DE TALLER (NUEVOS) ---

    suspend fun insertarTaller(taller: Taller) {
        tallerDao.insertarTaller(taller)
    }

    suspend fun updateTaller(taller: Taller) {
        tallerDao.updateTaller(taller)
    }

    suspend fun activarTaller(codTal: Int) {
        tallerDao.activarTaller(codTal)
    }

    suspend fun inactivarTaller(codTal: Int) {
        tallerDao.inactivarTaller(codTal)
    }

    suspend fun deleteTaller(taller: Taller) {
        tallerDao.deleteTaller(taller)
    }

    suspend fun eliminarLogicoTaller(codTal: Int) {
        tallerDao.eliminarLogicoTaller(codTal)
    }

    // Para leer usamos la Relación Completa (TallerConDetalles)
    fun getTalleresTodas(): Flow<List<TallerConDetalles>> {
        return tallerDao.getTalleresTodas()
    }

    fun getTalleresActivas(): Flow<List<TallerConDetalles>> {
        return tallerDao.getTalleresActivas()
    }

    fun getTalleresInactivas(): Flow<List<TallerConDetalles>> {
        return tallerDao.getTalleresInactivas()
    }

    fun getTalleresEliminadas(): Flow<List<TallerConDetalles>> {
        return tallerDao.getTalleresEliminadas()
    }

    fun getTaller(codTal: Int): Flow<TallerConDetalles?> {
        return tallerDao.getTaller(codTal)
    }

    // Filtros especiales
    fun getTalleresPorPlanta(codPla: Int): Flow<List<TallerConDetalles>> {
        return tallerDao.getTalleresPorPlanta(codPla)
    }

    fun getTalleresPorDepartamento(codDep: Int): Flow<List<TallerConDetalles>> {
        return tallerDao.getTalleresPorDepartamento(codDep)
    }

    // Acciones especiales
    // En Repository.kt

    suspend fun insertarDatosDePrueba(): Boolean {
        val totalRegistros = tallerDao.contarTotalRegistros()

        if (totalRegistros > 0) {
            return false
        }

        db.withTransaction {
            val p1 = Planta(nomPla = "Central", estPla = "A")
            val p2 = Planta(nomPla = "Costa", estPla = "A")
            val p3 = Planta(nomPla = "Sierra", estPla = "A")
            plantaDao.insertarPlanta(p1)
            plantaDao.insertarPlanta(p2)
            plantaDao.insertarPlanta(p3)

            val d1 = Departamento(nomDep = "Arequipa", estDep = "A")
            val d2 = Departamento(nomDep = "Tacna", estDep = "A")
            val d3 = Departamento(nomDep = "Moquegua", estDep = "A")
            departamentoDao.insertarDepartamento(d1)
            departamentoDao.insertarDepartamento(d2)
            departamentoDao.insertarDepartamento(d3)

            val tt1 = TipoTaller(nomTipTal = "Eléctrico", estTipTal = "A")
            val tt2 = TipoTaller(nomTipTal = "Mecánico", estTipTal = "A")
            val tt3 = TipoTaller(nomTipTal = "Pintura", estTipTal = "A")
            val tt4 = TipoTaller(nomTipTal = "Automotriz", estTipTal = "A")
            val tt5 = TipoTaller(nomTipTal = "Soldadura", estTipTal = "A")
            tipoTallerDao.insertarTipoTaller(tt1)
            tipoTallerDao.insertarTipoTaller(tt2)
            tipoTallerDao.insertarTipoTaller(tt3)
            tipoTallerDao.insertarTipoTaller(tt4)
            tipoTallerDao.insertarTipoTaller(tt5)

            val taller1 = Taller(
                nomTal = "Mantenimiento General",
                codPla = 1,    // Central
                codDep = 1,    // Arequipa
                codTipTal = 2, // Mecánico
                estTal = "A"
            )

            val taller2 = Taller(
                nomTal = "Electro Arequipa",
                codPla = 1,    // Central
                codDep = 1,    // Arequipa
                codTipTal = 1, // Eléctrico
                estTal = "A"
            )

            val taller3 = Taller(
                nomTal = "Pintura Tacna",
                codPla = 2,    // Costa
                codDep = 2,    // Tacna
                codTipTal = 3, // Pintura
                estTal = "A"
            )

            val taller4 = Taller(
                nomTal = "Servicio Automotriz Moquegua",
                codPla = 3,    // Sierra
                codDep = 3,    // Moquegua
                codTipTal = 4, // Automotriz
                estTal = "A"
            )

            val taller5 = Taller(
                nomTal = "Soldadura Costa",
                codPla = 2,    // Costa
                codDep = 1,    // Arequipa
                codTipTal = 5, // Soldadura
                estTal = "A"
            )

            val taller6 = Taller(
                nomTal = "Soporte Técnico Central",
                codPla = 1,    // Central
                codDep = 2,    // Tacna
                codTipTal = 1, // Eléctrico
                estTal = "A"
            )

            tallerDao.insertarTaller(taller1)
            tallerDao.insertarTaller(taller2)
            tallerDao.insertarTaller(taller3)
            tallerDao.insertarTaller(taller4)
            tallerDao.insertarTaller(taller5)
            tallerDao.insertarTaller(taller6)
        }
        return true
    }
    suspend fun borrarBaseDeDatosCompleta() {
        withContext(Dispatchers.IO) {
            db.clearAllTables()
            tallerDao.resetIds()
        }
    }
    suspend fun esBaseDeDatosVacia(): Boolean {
        return withContext(Dispatchers.IO) {
            val cantidad = tallerDao.contarTotalRegistros()
            cantidad == 0
        }
    }
}