package com.artlens.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.composables.ArtistDetailScreen
import com.artlens.view.viewmodels.ArtistViewModel

class ArtistDetailActivity : ComponentActivity() {

    // Inicializamos el ViewModel usando el Factory y el FacadeProvider
    private val artistViewModel: ArtistViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ID del artista que queremos mostrar desde el Intent
        val artistId = intent.getIntExtra("ARTIST_ID", 1)

        // Usar Jetpack Compose para la interfaz
        setContent {
            // Observamos el estado del LiveData dentro de una función composable
            val artistState by artistViewModel.artistLiveData.observeAsState()

            // Pantalla de detalle del artista
            ArtistDetailScreen(
                artist = artistState,
                onBackClick = { finish() },  // Acción para el botón de "Volver"
                onHomeClick = {
                    // Navegación al Home
                    finish()
                },
                onRecommendationClick = {
                    // Acción para ir a la vista de recomendaciones, puedes implementar la navegación
                }
            )

            // Llamamos al ViewModel para obtener los detalles del artista
            LaunchedEffect(Unit) {
                artistViewModel.fetchArtistDetail(artistId)
            }
        }
    }
}
