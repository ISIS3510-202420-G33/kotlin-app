package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.artlens.view.composables.RecommendationsScreen

class RecommendationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Llamada al Composable con la navegación
            RecommendationsScreen(onBackClick = {
                onBackPressed()
            }, onRecommendationClick = { recommendationId ->
                // Navegar a la vista de detalles de la obra con el ID por defecto 1
                val intent = Intent(this, ArtworkDetailActivity::class.java)
                intent.putExtra("id", recommendationId) // Pasamos el ID de la obra seleccionada
                startActivity(intent)
            })
        }
    }
}
