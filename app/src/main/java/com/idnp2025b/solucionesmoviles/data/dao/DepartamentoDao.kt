package com.idnp2025b.solucionesmoviles.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartamentoDao {

    // Insertar
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarDepartamento(departamento: Departamento)

    // Actualizar
    @Update
    suspend fun updateDepartamento(departamento: Departamento)

    @Query("UPDATE departamento SET estDep = 'A' WHERE codDep = :codDep")
    suspend fun activarDepartamento(codDep: Int)

    @Query("UPDATE departamento SET estDep = 'I' WHERE codDep = :codDep")
    suspend fun inactivarDepartamento(codDep: Int)

    // Eliminaciones
    @Delete
    suspend fun deleteDepartamento(departamento: Departamento)

    @Query("UPDATE departamento SET estDep = 'E' WHERE codDep = :codDep")
    suspend fun eliminarLogicoDepartamento(codDep: Int)

    // Obtener todas
    @Query("SELECT * FROM departamento WHERE estDep = 'A' or estDep = 'I' ORDER BY nomDep ASC")
    fun getDepartamentoTodas(): Flow<List<Departamento>>

    @Query("SELECT * FROM departamento WHERE estDep = 'A' ORDER BY nomDep ASC")
    fun getDepartamentoActivas(): Flow<List<Departamento>>

    @Query("SELECT * FROM departamento WHERE estDep = 'I' ORDER BY nomDep ASC")
    fun getDepartamentoInactivas(): Flow<List<Departamento>>

    @Query("SELECT * FROM departamento WHERE estDep = 'E' ORDER BY nomDep ASC")
    fun getDepartamentoEliminadas(): Flow<List<Departamento>>

    @Query("SELECT * FROM departamento WHERE codDep = :codDep")
    fun getDepartamento(codDep: Int): Flow<Departamento?>

}
