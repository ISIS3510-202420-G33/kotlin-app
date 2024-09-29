package com.artlens.view.activities

import android.annotation.SuppressLint
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
        val artworkId = intent.getIntExtra("id", 1)

        // Usar setContent para definir el layout con Jetpack Compose
        setContent {
            // Observar los datos de la obra y pasarlos a la función Composable
            val artworkState by artworkViewModel.artworkLiveData.observeAsState()

            // Configurar la estructura general de la pantalla con Scaffold
            Scaffold(

            ) {
                // Mostrar la pantalla de detalle de la obra
                ArtworkDetailScreen(
                    artwork = artworkState,
                    onBackClick = { onBackPressed() },  // Aquí pasamos la acción de "Atrás"
                    onMoreInfoClick = {
                        // Aquí puedes manejar la lógica para mostrar más información
                    }
                )

                // Llamar al ViewModel para obtener los detalles de la obra
                LaunchedEffect(Unit) {
                    artworkViewModel.fetchArtworkDetail(artworkId)
                }
            }
        }
    }
}
