package com.idnp2025b.solucionesmoviles.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.idnp2025b.solucionesmoviles.data.entities.Taller
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import kotlinx.coroutines.flow.Flow

@Dao
interface TallerDao {

    // Insertar
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarTaller(taller: Taller)

    // Actualizar-
    @Update
    suspend fun updateTaller(taller: Taller)

    @Query("UPDATE taller SET estTal = 'A' WHERE codTal = :codTal")
    suspend fun activarTaller(codTal: Int)

    @Query("UPDATE taller SET estTal = 'I' WHERE codTal = :codTal")
    suspend fun inactivarTaller(codTal: Int)

    // Eliminaciones
    @Delete
    suspend fun deleteTaller(taller: Taller)

    @Query("UPDATE taller SET estTal = 'E' WHERE codTal = :codTal")
    suspend fun eliminarLogicoTaller(codTal: Int)

    // Obtener activos e inactivos con sus nombres de planta/depto/tipo
    @Transaction
    @Query("SELECT * FROM taller WHERE estTal = 'A' OR estTal = 'I' ORDER BY nomTal ASC")
    fun getTalleresTodas(): Flow<List<TallerConDetalles>>

    // Solo activos
    @Transaction
    @Query("SELECT * FROM taller WHERE estTal = 'A' ORDER BY nomTal ASC")
    fun getTalleresActivas(): Flow<List<TallerConDetalles>>

    // Solo inactivos
    @Transaction
    @Query("SELECT * FROM taller WHERE estTal = 'I' ORDER BY nomTal ASC")
    fun getTalleresInactivas(): Flow<List<TallerConDetalles>>

    // Solo eliminados (papelera)
    @Transaction
    @Query("SELECT * FROM taller WHERE estTal = 'E' ORDER BY nomTal ASC")
    fun getTalleresEliminadas(): Flow<List<TallerConDetalles>>

    // Obtener un taller específico por ID con todos sus detalles
    @Transaction
    @Query("SELECT * FROM taller WHERE codTal = :codTal")
    fun getTaller(codTal: Int): Flow<TallerConDetalles?>

    // --- CONSULTAS ESPECIALES (FILTROS POR FK) ---
    @Transaction
    @Query("SELECT * FROM taller WHERE codPla = :codPla AND estTal = 'A' ORDER BY nomTal ASC")
    fun getTalleresPorPlanta(codPla: Int): Flow<List<TallerConDetalles>>

    @Transaction
    @Query("SELECT * FROM taller WHERE codDep = :codDep AND estTal = 'A' ORDER BY nomTal ASC")
    fun getTalleresPorDepartamento(codDep: Int): Flow<List<TallerConDetalles>>
}