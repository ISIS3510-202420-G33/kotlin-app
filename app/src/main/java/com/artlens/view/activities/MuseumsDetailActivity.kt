package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.viewmodels.MuseumsDetailViewModel
import com.artlens.view.composables.MuseumDetailScreen

class MuseumsDetailActivity : ComponentActivity() {

    private val museumsDetailViewModel: MuseumsDetailViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val museumId = intent.getIntExtra("MUSEUM_ID", 1)

        setContent {
            val museumState by museumsDetailViewModel.getMuseumDetail(museumId).observeAsState()
            val artworkState by museumsDetailViewModel.getArtworksByMuseum(museumId).observeAsState(emptyList()) // Observamos las obras de arte

            // Verificar si hay artworks y URLs válidas
            Log.d("MuseumsDetailActivity", "Number of artworks: ${artworkState.size}")
            artworkState.forEach { artwork ->
                Log.d("MuseumsDetailActivity", "Artwork URL: ${artwork.fields.image}")
            }
            // Obtenemos las URLs de las obras de arte
            val artworkUrls = artworkState.map { it.fields.image }.take(5) // Tomamos solo 5 obras

            MuseumDetailScreen(
                museum = museumState,
                artworkUrls = artworkUrls,  // Pasamos las URLs al carrusel
                onBackClick = {
                    // Regresar a la lista de museos
                    val intent = Intent(this, MuseumsListActivity::class.java)
                    startActivity(intent)
                },
                onHomeClick = {
                    // Ir a la MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}
