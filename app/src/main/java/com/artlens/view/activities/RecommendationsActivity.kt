package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.artlens.viewmodels.RecommendationsViewModel
import com.artlens.view.composables.RecommendationsScreen

class RecommendationsActivity : AppCompatActivity() {

    private val recommendationsViewModel: RecommendationsViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el userId desde UserPreferences
        val userId = UserPreferences.getPk() ?: -1

        // Cargar la obra más likeada
        recommendationsViewModel.fetchMostLikedArtwork()

        if (userId >= 0) {
            // Usuario logueado: cargar las recomendaciones además de la obra más likeada
            recommendationsViewModel.fetchArtworkRecommendations(userId)
        }

        // Establecer el contenido usando Jetpack Compose
        setContent {
            RecommendationsScreen(
                recommendationsViewModel = recommendationsViewModel,
                onBackClick = { finish() },
                isLoggedIn = userId >= 0,  // Indicamos si el usuario está logueado o no
                onRecommendationClick = { artworkId ->
                    val intent = Intent(this, ArtworkDetailActivity::class.java)
                    intent.putExtra("id", artworkId)
                    startActivity(intent)
                }
            )
        }
    }
}
