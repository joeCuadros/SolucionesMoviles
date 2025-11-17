package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FiltroTipoTaller {
    TODAS, ACTIVAS, INACTIVAS, ELIMINADAS
}

@HiltViewModel
class TipoTallerViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _tipoTalleres = MutableStateFlow<List<TipoTaller>>(emptyList())
    val tipoTalleres: StateFlow<List<TipoTaller>> = _tipoTalleres.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _filtroActual = MutableStateFlow(FiltroTipoTaller.ACTIVAS)
    val filtroActual: StateFlow<FiltroTipoTaller> = _filtroActual.asStateFlow()

    init {
        cargarTipoTalleres(FiltroTipoTaller.ACTIVAS)
    }

    fun cargarTipoTalleres(filtro: FiltroTipoTaller = _filtroActual.value) {
        _filtroActual.value = filtro
        viewModelScope.launch {
            val flow = when (filtro) {
                FiltroTipoTaller.TODAS -> repository.getTipoTallersTodas()
                FiltroTipoTaller.ACTIVAS -> repository.getTipoTallersActivas()
                FiltroTipoTaller.INACTIVAS -> repository.getTipoTallersInactivas()
                FiltroTipoTaller.ELIMINADAS -> repository.getTipoTallersEliminadas()
            }
            flow.collect { listaTipoTalleres ->
                _tipoTalleres.value = listaTipoTalleres
            }
        }
    }

    fun obtenerTipoTaller(codTipTal: Int): Flow<TipoTaller?>{
        return repository.getTipoTaller(codTipTal)
    }

    fun agregarTipoTaller(nombre: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.insertarTipoTaller(TipoTaller(nomTipTal = nombre, estTipTal = "A"))
                _uiState.value = UiState.Success("Tipo de Taller agregado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre del tipo de taller ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun actualizarTipoTaller(tipoTaller: TipoTaller) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.updateTipoTaller(tipoTaller)
                _uiState.value = UiState.Success("Tipo de Taller actualizado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre del tipo de taller ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al actualizar: ${e.message}")
            }
        }
    }

    fun activarTipoTaller(codTipTal: Int) {
        viewModelScope.launch {
            try {
                repository.activarTipoTaller(codTipTal)
                _uiState.value = UiState.Success("Tipo de Taller activado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al activar: ${e.message}")
            }
        }
    }

    fun inactivarTipoTaller(codTipTal: Int) {
        viewModelScope.launch {
            try {
                repository.inactivarTipoTaller(codTipTal)
                _uiState.value = UiState.Success("Tipo de Taller inactivado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al inactivar: ${e.message}")
            }
        }
    }

    fun eliminarLogicoTipoTaller(codTipTal: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarLogicoTipoTaller(codTipTal)
                _uiState.value = UiState.Success("Tipo de Taller eliminado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun deleteTipoTaller(tipoTaller: TipoTaller) {
        viewModelScope.launch {
            try {
                repository.deleteTipoTaller(tipoTaller)
                _uiState.value = UiState.Success("Tipo de Taller eliminado permanentemente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}