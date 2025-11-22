package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    // 👇 Protección contra clics rápidos
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    // 👇 Job para cancelar la carga anterior
    private var cargarJob: Job? = null

    init {
        cargarTipoTalleres(FiltroTipoTaller.ACTIVAS)
    }

    fun cargarTipoTalleres(filtro: FiltroTipoTaller = _filtroActual.value) {
        // Cancela el job anterior si existe
        cargarJob?.cancel()

        _filtroActual.value = filtro

        cargarJob = viewModelScope.launch {
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
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.insertarTipoTaller(TipoTaller(nomTipTal = nombre, estTipTal = "A"))
                _uiState.value = UiState.Success("Tipo de Taller '$nombre' creado exitosamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El tipo de taller '$nombre' ya existe en el sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo crear el tipo de taller '$nombre': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun actualizarTipoTaller(tipoTaller: TipoTaller) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.updateTipoTaller(tipoTaller)
                _uiState.value = UiState.Success("Tipo de Taller '${tipoTaller.nomTipTal}' actualizado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("Ya existe otro tipo de taller con el nombre '${tipoTaller.nomTipTal}'")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo actualizar '${tipoTaller.nomTipTal}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun activarTipoTaller(codTipTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.activarTipoTaller(codTipTal)
                _uiState.value = UiState.Success("Tipo de Taller activado exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo activar el tipo de taller: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun inactivarTipoTaller(codTipTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.inactivarTipoTaller(codTipTal)
                _uiState.value = UiState.Success("Tipo de Taller inactivado correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo inactivar el tipo de taller: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun eliminarLogicoTipoTaller(codTipTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.eliminarLogicoTipoTaller(codTipTal)
                _uiState.value = UiState.Success("Tipo de Taller eliminado exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar el tipo de taller: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun deleteTipoTaller(tipoTaller: TipoTaller) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.deleteTipoTaller(tipoTaller)
                _uiState.value = UiState.Success("Tipo de Taller '${tipoTaller.nomTipTal}' eliminado permanentemente del sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar permanentemente '${tipoTaller.nomTipTal}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}