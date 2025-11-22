package com.idnp2025b.solucionesmoviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfiguracionViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _isDbEmpty = MutableStateFlow(false)
    val isDbEmpty: StateFlow<Boolean> = _isDbEmpty.asStateFlow()

    init {
        verificarEstadoBD()
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

    fun verificarEstadoBD() {
        viewModelScope.launch {
            try {
                val estaVacia = repository.esBaseDeDatosVacia()
                _isDbEmpty.value = estaVacia
            } catch (e: Exception) {
                _isDbEmpty.value = false
            }
        }
    }

    fun inicializarDatosDePrueba() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val seInserto = repository.insertarDatosDePrueba()

                if (seInserto) {
                    _uiState.value = UiState.Success("Base de datos inicializada con éxito")
                    _isDbEmpty.value = false
                } else {
                    _uiState.value = UiState.Error("La base de datos NO está vacía. Borra los datos primero.")
                }

            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error crítico al inicializar: ${e.message}")
            }
        }
    }

    fun borrarTodo() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.borrarBaseDeDatosCompleta()
                _uiState.value = UiState.Success("Se han eliminado todos los datos")
                _isDbEmpty.value = true
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error al borrar: ${e.message}")
            }
        }
    }
}