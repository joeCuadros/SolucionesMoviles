package com.idnp2025b.solucionesmoviles.ui.components.general

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.idnp2025b.solucionesmoviles.ui.theme.colorRojoEstado

@Composable
fun DialogoEliminar(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    nombreItem: String,
    esPermanente: Boolean
) {
    val titulo = if (esPermanente) "¿Eliminar permanentemente?" else "¿Mover a la papelera?"
    val mensaje = if (esPermanente) {
        "Estás a punto de borrar '$nombreItem' de la base de datos. Esta acción NO se puede deshacer. ¿Continuar?"
    } else {
        "'$nombreItem' pasará a la lista de eliminados. Podrás restaurarlo más tarde si lo necesitas."
    }
    val textoBoton = if (esPermanente) "Eliminar definitivamente" else "Eliminar"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = mensaje)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorRojoEstado,
                    contentColor = Color.White
                )
            ) {
                Text(textoBoton)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}