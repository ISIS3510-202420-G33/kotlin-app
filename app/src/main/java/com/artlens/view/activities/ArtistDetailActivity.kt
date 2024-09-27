package com.artlens.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.viewmodels.ArtistViewModel
import com.artlens.view.composables.ArtistDetailScreen

class ArtistDetailActivity : ComponentActivity() {

    // Inicializamos el ViewModel usando el Factory y el FacadeProvider
    private val artistViewModel: ArtistViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)  // Usamos el Facade singleton aquí
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ID del artista que queremos mostrar
        val artistId = intent.getIntExtra("ARTIST_ID", 1)

        // Usar Jetpack Compose para la interfaz
        setContent {
            // Observamos el LiveData dentro de una función composable
            val artistState by artistViewModel.artistLiveData.observeAsState()

            // Colocamos la pantalla de detalle y el botón para volver
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Mostrar la pantalla de detalle del artista
                ArtistDetailScreen(artist = artistState)

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para volver
                Button(onClick = { finish() }) {
                    Text(text = "Volver")
                }
            }

            // Llamar al ViewModel para obtener los detalles del artista
            artistViewModel.fetchArtistDetail(artistId)
        }
    }
}
