package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.view.composables.ArtistListScreen
import com.artlens.view.viewmodels.ArtistListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory

class ArtistListActivity : ComponentActivity() {

    // Obtenemos el ViewModel de la lista de artistas
    private val artistListViewModel: ArtistListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Llamamos a la función para obtener todos los artistas
        artistListViewModel.fetchAllArtists()

        setContent {
            // Observa los datos de la lista de artistas desde el ViewModel
            val artists = artistListViewModel.artistsLiveData.observeAsState(emptyList()).value

            // Pasamos la lista de artistas y las acciones a la pantalla de ArtistListScreen
            ArtistListScreen(
                artists = artists,  // Lista de artistas
                onBackClick = { onBackPressed() },
                onHomeClick = {
                    // Aquí puedes navegar a la pantalla de inicio o home
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                },
                onRecommendationClick = {
                    // Aquí puedes navegar a la pantalla de recomendaciones
                    val intent = Intent(this, RecommendationsActivity::class.java)
                    startActivity(intent)
                },
                onArtistClick = { artistId ->
                    // Aquí navegas a la pantalla de detalles del artista
                    val intent = Intent(this, ArtistDetailActivity::class.java)
                    intent.putExtra("ARTIST_ID", artistId)
                    startActivity(intent)
                }
            )
        }
    }
}
