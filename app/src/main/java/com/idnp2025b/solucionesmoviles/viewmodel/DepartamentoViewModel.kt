package com.idnp2025b.solucionesmoviles.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()
    private var cargarJob: Job? = null

    init {
        cargarDepartamentos(FiltroDepartamento.ACTIVAS)
    }

    fun cargarDepartamentos(filtro: FiltroDepartamento = _filtroActual.value) {
        cargarJob?.cancel()
        _filtroActual.value = filtro
        cargarJob = viewModelScope.launch {
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
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.insertarDepartamento(Departamento(nomDep = nombre, estDep = "A"))
                _uiState.value = UiState.Success("Departamento '$nombre' creado exitosamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("El departamento '$nombre' ya existe en el sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo crear el departamento '$nombre': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun actualizarDepartamento(departamento: Departamento) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.updateDepartamento(departamento)
                _uiState.value = UiState.Success("Departamento '${departamento.nomDep}' actualizado correctamente")
            } catch (e: SQLiteConstraintException) {
                _uiState.value = UiState.Error("Ya existe otro departamento con el nombre '${departamento.nomDep}'")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo actualizar '${departamento.nomDep}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun activarDepartamento(codDep: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.activarDepartamento(codDep)
                _uiState.value = UiState.Success("Departamento activado exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo activar el departamento: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun inactivarDepartamento(codDep: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.inactivarDepartamento(codDep)
                _uiState.value = UiState.Success("Departamento inactivado correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo inactivar el departamento: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun eliminarLogicoDepartamento(codDep: Int) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.eliminarLogicoDepartamento(codDep)
                _uiState.value = UiState.Success("Departamento eliminado exitosamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar el departamento: ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun deleteDepartamento(departamento: Departamento) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _uiState.value = UiState.Loading
            try {
                repository.deleteDepartamento(departamento)
                _uiState.value = UiState.Success("Departamento '${departamento.nomDep}' eliminado permanentemente del sistema")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("No se pudo eliminar permanentemente '${departamento.nomDep}': ${e.message}")
            } finally {
                _isProcessing.value = false
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}