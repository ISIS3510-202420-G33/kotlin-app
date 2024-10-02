package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artlens.view.composables.RecommendationsScreen
import com.artlens.view.viewmodels.ArtworkListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory

class RecommendationsActivity : ComponentActivity() {

    // Obtenemos el ViewModel de la lista de obras de arte
    private val artworkListViewModel: ArtworkListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Llamamos a la función para obtener todas las obras de arte
        artworkListViewModel.fetchAllArtworks()

        setContent {
            RecommendationsScreen(
                onBackClick = {
                    onBackPressed()
                },
                onRecommendationClick = { recommendationId ->
                    // Aquí navegas a la pantalla de detalles
                    val intent = Intent(this, ArtworkDetailActivity::class.java)
                    intent.putExtra("id", recommendationId)  // Pasamos el ID de la obra seleccionada
                    startActivity(intent)
                },
                artworkListViewModel = artworkListViewModel
            )
        }
    }
}
