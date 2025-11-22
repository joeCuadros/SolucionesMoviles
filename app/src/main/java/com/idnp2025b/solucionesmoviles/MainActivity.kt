package com.idnp2025b.solucionesmoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.idnp2025b.solucionesmoviles.ui.navigation.MainScreen
import com.idnp2025b.solucionesmoviles.ui.theme.SolucionesMovilesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SolucionesMovilesTheme {
                    Surface {
                        MainScreen() //usar pantalla principal
                    }
                }
            }
        }
    }
}