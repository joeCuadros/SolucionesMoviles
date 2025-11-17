package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FiltroDepartamento {
    TODAS, ACTIVAS, INACTIVAS, ELIMINADAS
}

@HiltViewModel
class DepartamentoViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _departamentos = MutableStateFlow<List<Departamento>>(emptyList())
    val departamentos: StateFlow<List<Departamento>> = _departamentos.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _filtroActual = MutableStateFlow(FiltroDepartamento.ACTIVAS)
    val filtroActual: StateFlow<FiltroDepartamento> = _filtroActual.asStateFlow()

    init {
        cargarDepartamentos(FiltroDepartamento.ACTIVAS)
    }

    fun cargarDepartamentos(filtro: FiltroDepartamento = _filtroActual.value) {
        _filtroActual.value = filtro
        viewModelScope.launch {
            val flow = when (filtro) {
                FiltroDepartamento.TODAS -> repository.getDepartamentoTodas()
                FiltroDepartamento.ACTIVAS -> repository.getDepartamentoActivas()
                FiltroDepartamento.INACTIVAS -> repository.getDepartamentoInactivas()
                FiltroDepartamento.ELIMINADAS -> repository.getDepartamentoEliminadas()
            }
            flow.collect { listaDepartamentos ->
                _departamentos.value = listaDepartamentos
            }
        }
    }

    fun obtenerDepartamento(codDep: Int): Flow<Departamento?>{
        return repository.getDepartamento(codDep)
    }

    fun agregarDepartamento(nombre: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                // Asumo que tu entidad Departamento tiene un constructor
                // que se puede inicializar solo con nomDep y estDep
                repository.insertarDepartamento(Departamento(nomDep = nombre, estDep = "A"))
                _uiState.value = UiState.Success("Departamento agregado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre del departamento ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al guardar: ${e.message}")
            }
        }
    }

    fun actualizarDepartamento(departamento: Departamento) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.updateDepartamento(departamento)
                _uiState.value = UiState.Success("Departamento actualizado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El nombre del departamento ya existe")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al actualizar: ${e.message}")
            }
        }
    }

    fun activarDepartamento(codDep: Int) {
        viewModelScope.launch {
            try {
                repository.activarDepartamento(codDep)
                _uiState.value = UiState.Success("Departamento activado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al activar: ${e.message}")
            }
        }
    }

    fun inactivarDepartamento(codDep: Int) {
        viewModelScope.launch {
            try {
                repository.inactivarDepartamento(codDep)
                _uiState.value = UiState.Success("Departamento inactivado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al inactivar: ${e.message}")
            }
        }
    }

    fun eliminarLogicoDepartamento(codDep: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarLogicoDepartamento(codDep)
                _uiState.value = UiState.Success("Departamento eliminado")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun deleteDepartamento(departamento: Departamento) {
        viewModelScope.launch {
            try {
                repository.deleteDepartamento(departamento)
                _uiState.value = UiState.Success("Departamento eliminado permanentemente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al eliminar: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}