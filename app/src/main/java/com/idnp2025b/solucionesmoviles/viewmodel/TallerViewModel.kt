package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Taller
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FiltroTaller {
    TODAS, ACTIVAS, INACTIVAS, ELIMINADAS
}

@HiltViewModel
class TallerViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // OJO: Aquí usamos TallerConDetalles para poder mostrar nombres en la lista
    private val _talleres = MutableStateFlow<List<TallerConDetalles>>(emptyList())
    val talleres: StateFlow<List<TallerConDetalles>> = _talleres.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _filtroActual = MutableStateFlow(FiltroTaller.ACTIVAS)
    val filtroActual: StateFlow<FiltroTaller> = _filtroActual.asStateFlow()

    // 👇 Protección contra clics rápidos
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    // 👇 Jobs para cancelar las cargas anteriores
    private var cargarJob: Job? = null
    private var cargarPorPlantaJob: Job? = null

    init {
        cargarTalleres(FiltroTaller.ACTIVAS)
    }

    fun cargarTalleres(filtro: FiltroTaller = _filtroActual.value) {
        // Cancela los jobs anteriores si existen
        cargarJob?.cancel()
        cargarPorPlantaJob?.cancel()

        _filtroActual.value = filtro

        cargarJob = viewModelScope.launch {
            val flow = when (filtro) {
                FiltroTaller.TODAS -> repository.getTalleresTodas()
                FiltroTaller.ACTIVAS -> repository.getTalleresActivas()
                FiltroTaller.INACTIVAS -> repository.getTalleresInactivas()
                FiltroTaller.ELIMINADAS -> repository.getTalleresEliminadas()
            }
            flow.collect { listaTalleres ->
                _talleres.value = listaTalleres
            }
        }
    }

    // Métodos extra para filtros específicos (opcional, pero muy útil)
    fun cargarTalleresPorPlanta(codPla: Int) {
        // Cancela los jobs anteriores si existen
        cargarJob?.cancel()
        cargarPorPlantaJob?.cancel()

        cargarPorPlantaJob = viewModelScope.launch {
            repository.getTalleresPorPlanta(codPla).collect { lista ->
                _talleres.value = lista
            }
        }
    }

    fun obtenerTaller(codTal: Int): Flow<TallerConDetalles?> {
        return repository.getTaller(codTal)
    }

    // Para agregar necesitamos los IDs de las relaciones
    fun agregarTaller(nombre: String, codPla: Int, codDep: Int, codTipTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                val nuevoTaller = Taller(
                    nomTal = nombre,
                    codPla = codPla,
                    codDep = codDep,
                    codTipTal = codTipTal,
                    estTal = "A" // Por defecto activo
                )
                repository.insertarTaller(nuevoTaller)
                _uiState.value = UiState.Success("Taller '$nombre' creado exitosamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El taller '$nombre' ya existe en el sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo crear el taller '$nombre': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun actualizarTaller(taller: Taller) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.updateTaller(taller)
                _uiState.value = UiState.Success("Taller '${taller.nomTal}' actualizado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("Ya existe otro taller con el nombre '${taller.nomTal}'")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo actualizar '${taller.nomTal}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun activarTaller(codTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.activarTaller(codTal)
                _uiState.value = UiState.Success("Taller activado exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo activar el taller: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun inactivarTaller(codTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.inactivarTaller(codTal)
                _uiState.value = UiState.Success("Taller inactivado correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo inactivar el taller: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun eliminarLogicoTaller(codTal: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.eliminarLogicoTaller(codTal)
                _uiState.value = UiState.Success("Taller eliminado exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar el taller: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun deleteTaller(taller: Taller) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.deleteTaller(taller)
                _uiState.value = UiState.Success("Taller '${taller.nomTal}' eliminado permanentemente del sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar permanentemente '${taller.nomTal}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}