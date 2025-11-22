package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FiltroPlanta {
    TODAS, ACTIVAS, INACTIVAS, ELIMINADAS
}

@HiltViewModel
class PlantaViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _plantas = MutableStateFlow<List<Planta>>(emptyList())
    val plantas: StateFlow<List<Planta>> = _plantas.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _filtroActual = MutableStateFlow(FiltroPlanta.ACTIVAS)
    val filtroActual: StateFlow<FiltroPlanta> = _filtroActual.asStateFlow()

    // 👇 Protección contra clics rápidos
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    // 👇 Job para cancelar la carga anterior
    private var cargarJob: Job? = null

    init {
        cargarPlantas(FiltroPlanta.ACTIVAS)
    }

    fun cargarPlantas(filtro: FiltroPlanta = _filtroActual.value) {
        // Cancela el job anterior si existe
        cargarJob?.cancel()

        _filtroActual.value = filtro

        cargarJob = viewModelScope.launch {
            val flow = when (filtro) {
                FiltroPlanta.TODAS -> repository.getPlantasTodas()
                FiltroPlanta.ACTIVAS -> repository.getPlantasActivas()
                FiltroPlanta.INACTIVAS -> repository.getPlantasInactivas()
                FiltroPlanta.ELIMINADAS -> repository.getPlantasEliminadas()
            }
            flow.collect { listaPlantas ->
                _plantas.value = listaPlantas
            }
        }
    }

    fun obtenerPlanta(codPla: Int): Flow<Planta?>{
        return repository.getPlanta(codPla)
    }

    fun agregarPlanta(nombre: String) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.insertarPlanta(Planta(nomPla = nombre, estPla = "A"))
                _uiState.value = UiState.Success("Planta '$nombre' creada exitosamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("La planta '$nombre' ya existe en el sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo crear la planta '$nombre': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun actualizarPlanta(planta: Planta) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.updatePlanta(planta)
                _uiState.value = UiState.Success("Planta '${planta.nomPla}' actualizada correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("Ya existe otra planta con el nombre '${planta.nomPla}'")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo actualizar '${planta.nomPla}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun activarPlanta(codPla: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.activarPlanta(codPla)
                _uiState.value = UiState.Success("Planta activada exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo activar la planta: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun inactivarPlanta(codPla: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.inactivarPlanta(codPla)
                _uiState.value = UiState.Success("Planta inactivada correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo inactivar la planta: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun eliminarLogicoPlanta(codPla: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.eliminarLogicoPlanta(codPla)
                _uiState.value = UiState.Success("Planta eliminada exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar la planta: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun deletePlanta(planta: Planta) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.deletePlanta(planta)
                _uiState.value = UiState.Success("Planta '${planta.nomPla}' eliminada permanentemente del sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar permanentemente '${planta.nomPla}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}