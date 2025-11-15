package com.idnp2025b.solucionesmoviles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantaViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    val plantas = repo.getPlantasTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() //iniciar vacia
        )

    fun agregar(nombre: String) = viewModelScope.launch {
        repo.insertarPlanta(
            Planta(
                nomPla = nombre,
                estPla = "A"
            )
        )
    }
}