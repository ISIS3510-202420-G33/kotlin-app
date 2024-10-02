package com.artlens.view.activities

import android.content.Intent
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
import com.artlens.view.viewmodels.ArtworkListViewModel

class ArtistDetailActivity : ComponentActivity() {

    // Inicializamos el ViewModel usando el Factory y el FacadeProvider
    private val artistViewModel: ArtistViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private val artworkListViewModel: ArtworkListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ID del artista que queremos mostrar desde el Intent
        val artistId = intent.getIntExtra("ARTIST_ID", 1)

        setContent {
            val artistState by artistViewModel.artistLiveData.observeAsState()
            val artworksState by artworkListViewModel.artworksLiveData.observeAsState(emptyList())  // Observa las obras del artista

            // Pantalla de detalle del artista con las obras de arte
            ArtistDetailScreen(
                artist = artistState,
                artworks = artworksState,
                onBackClick = { onBackPressed() },  // Acción para el botón de "Volver"
                onHomeClick = {
                    // Navegación al Home
                    navigateToMainActivity()
                },
                onArtworkClick = { artworkId ->
                    val intent = Intent(this, ArtworkDetailActivity::class.java)
                    intent.putExtra("id", artworkId)  // Pasamos el ID de la obra seleccionada
                    startActivity(intent)
                }
            )

            // Llamar al ViewModel para obtener los detalles del artista y sus obras
            LaunchedEffect(Unit) {
                artistViewModel.fetchArtistDetail(artistId)
                artworkListViewModel.fetchArtworksByArtist(artistId)  // Obtener las obras del artista
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
