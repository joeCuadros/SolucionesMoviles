package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}

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

    init {
        cargarPlantas(FiltroPlanta.ACTIVAS)
    }

    fun cargarPlantas(filtro: FiltroPlanta = _filtroActual.value) {
        _filtroActual.value = filtro
        viewModelScope.launch {
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
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.insertarPlanta(Planta(nomPla = nombre, estPla = "A"))
                _uiState.value = UiState.Success("Planta agregada correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre de la planta ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun actualizarPlanta(planta: Planta) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.updatePlanta(planta)
                _uiState.value = UiState.Success("Planta actualizada correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre de la planta ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al actualizar: ${e.message}")
            }
        }
    }

    fun activarPlanta(codPla: Int) {
        viewModelScope.launch {
            try {
                repository.activarPlanta(codPla)
                _uiState.value = UiState.Success("Planta activada")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al activar: ${e.message}")
            }
        }
    }

    fun inactivarPlanta(codPla: Int) {
        viewModelScope.launch {
            try {
                repository.inactivarPlanta(codPla)
                _uiState.value = UiState.Success("Planta inactivada")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al inactivar: ${e.message}")
            }
        }
    }

    fun eliminarLogico(codPla: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarLogico(codPla)
                _uiState.value = UiState.Success("Planta eliminada")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun deletePlanta(planta: Planta) {
        viewModelScope.launch {
            try {
                repository.deletePlanta(planta)
                _uiState.value = UiState.Success("Planta eliminada permanentemente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}