package com.idnp2025b.solucionesmoviles.data

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_bodega_prefs", Context.MODE_PRIVATE)

    fun guardarUsuario(usuario: String) {
        prefs.edit().putString("usuario_actual", usuario).apply()
    }

    fun obtenerUsuario(): String? {
        return prefs.getString("usuario_actual", null)
    }

    fun borrarSesion() {
        prefs.edit().remove("usuario_actual").apply()
    }
}