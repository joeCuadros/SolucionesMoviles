package com.idnp2025b.solucionesmoviles.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoTallerDao {
    // Insertar
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarTipoTaller(planta: TipoTaller)

    // Actualizar
    @Update
    suspend fun updateTipoTaller(planta: TipoTaller)

    @Query("UPDATE tipo_taller SET estTipTal = 'A' WHERE codTipTal = :codTipTal")
    suspend fun activarTipoTaller(codTipTal: Int)

    @Query("UPDATE tipo_taller SET estTipTal = 'I' WHERE codTipTal = :codTipTal")
    suspend fun inactivarTipoTaller(codTipTal: Int)

    // Eliminaciones
    @Delete
    suspend fun deleteTipoTaller(planta: TipoTaller)

    @Query("UPDATE tipo_taller SET estTipTal = 'E' WHERE codTipTal = :codTipTal")
    suspend fun eliminarLogicoTipoTaller(codTipTal: Int)

    // Obtener todas
    @Query("SELECT * FROM tipo_taller WHERE estTipTal = 'A' or estTipTal = 'I' ORDER BY nomTipTal ASC")
    fun getTipoTallersTodas(): Flow<List<TipoTaller>>

    @Query("SELECT * FROM tipo_taller WHERE estTipTal = 'A' ORDER BY nomTipTal ASC")
    fun getTipoTallersActivas(): Flow<List<TipoTaller>>

    @Query("SELECT * FROM tipo_taller WHERE estTipTal = 'I' ORDER BY nomTipTal ASC")
    fun getTipoTallersInactivas(): Flow<List<TipoTaller>>

    @Query("SELECT * FROM tipo_taller WHERE estTipTal = 'E' ORDER BY nomTipTal ASC")
    fun getTipoTallersEliminadas(): Flow<List<TipoTaller>>

    @Query("SELECT * FROM tipo_taller WHERE codTipTal = :codTipTal")
    fun getTipoTaller(codTipTal: Int): Flow<TipoTaller?>
}