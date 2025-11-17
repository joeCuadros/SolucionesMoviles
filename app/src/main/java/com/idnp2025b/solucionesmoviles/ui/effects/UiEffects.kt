package com.idnp2025b.solucionesmoviles.ui.effects

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.idnp2025b.solucionesmoviles.viewmodel.UiState
@Composable
fun HandleUiStateEffects(
    uiState: UiState,
    onResetState: () -> Unit,
    onSuccess: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is UiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                onSuccess()
                onResetState()
            }
            is UiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                onResetState()
            }
            else -> { /* No hace nada en Idle o Loading */ }
        }
    }
}

