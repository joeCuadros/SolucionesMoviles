package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Taller
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        cargarTalleres(FiltroTaller.ACTIVAS)
    }

    fun cargarTalleres(filtro: FiltroTaller = _filtroActual.value) {
        _filtroActual.value = filtro
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
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
                _uiState.value = UiState.Success("Taller agregado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre del taller ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun actualizarTaller(taller: Taller) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.updateTaller(taller)
                _uiState.value = UiState.Success("Taller actualizado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre del taller ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al actualizar: ${e.message}")
            }
        }
    }

    fun activarTaller(codTal: Int) {
        viewModelScope.launch {
            try {
                repository.activarTaller(codTal)
                _uiState.value = UiState.Success("Taller activado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al activar: ${e.message}")
            }
        }
    }

    fun inactivarTaller(codTal: Int) {
        viewModelScope.launch {
            try {
                repository.inactivarTaller(codTal)
                _uiState.value = UiState.Success("Taller inactivado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al inactivar: ${e.message}")
            }
        }
    }

    fun eliminarLogicoTaller(codTal: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarLogicoTaller(codTal)
                _uiState.value = UiState.Success("Taller eliminado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun deleteTaller(taller: Taller) {
        viewModelScope.launch {
            try {
                repository.deleteTaller(taller)
                _uiState.value = UiState.Success("Taller eliminado permanentemente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}