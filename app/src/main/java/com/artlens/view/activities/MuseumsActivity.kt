package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.artlens.view.composables.MuseumsScreen

class MuseumsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuseumsScreen(
                onMapClick = {
                    // Acción al hacer clic en "View Map", probablemente podrías iniciar otra Activity
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                },
                onMuseumClick = {
                    // Acción al hacer clic en un museo o lista de museos
                }
            )
        }
    }
}
