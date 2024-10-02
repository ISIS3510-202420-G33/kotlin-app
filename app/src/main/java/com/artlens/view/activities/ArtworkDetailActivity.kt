// Archivo: ArtworkDetailActivity.kt
package com.artlens.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.data.models.ArtworkResponse
import com.artlens.view.viewmodels.ArtworkViewModel
import com.artlens.view.composables.ArtworkDetailScreen

class ArtworkDetailActivity : ComponentActivity() {

    // Inicializamos el ViewModel correctamente usando el singleton de FacadeProvider
    private val artworkViewModel: ArtworkViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade) // Pasamos el singleton de la fachada
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ID de la obra que queremos mostrar
        val artworkId = intent.getIntExtra("id", 2)

        // Usar setContent para definir el layout con Jetpack Compose
        setContent {
            val artworkState by artworkViewModel.artworkLiveData.observeAsState()
            val isLiked by artworkViewModel.isLiked.observeAsState(false)

            // Configurar la estructura general de la pantalla con Scaffold
            Scaffold(

            ) {
                // Mostrar la pantalla de detalle de la obra
               /* ArtworkDetailScreen(
                    onBackClick = { onBackPressed() },  // Aquí pasamos la acción de "Atrás"
                    onMoreInfoClick = { artistId ->  // Aquí se recibe el artistId
                        // Lanza la actividad de detalles del artista
                        val intent = Intent(this, ArtworkDetailActivity::class.java)
                        intent.putExtra("ARTIST_ID", artistId)
                        startActivity(intent)
                    }
                )*/

                // Llamar al ViewModel para obtener los detalles de la obra
                LaunchedEffect(Unit) {
                    artworkViewModel.fetchArtworkDetail(artworkId)
                }
            }
        }
    }

    private fun ArtworkDetailScreen(artwork: ArtworkResponse?, onBackClick: () -> Unit, onMoreInfoClick: (Int) -> Unit) {

    }
}
