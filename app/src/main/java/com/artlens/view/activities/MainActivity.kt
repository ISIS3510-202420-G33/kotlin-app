package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.view.composables.MainScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory

class MainActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Observar el estado de los museos
            val museums by museumsViewModel.museumsLiveData.observeAsState(emptyList())

            // Extraer las URLs de las imágenes de los museos
            val imageUrls = museums.map { it.fields.image }

            // Llamamos a MainScreen y le pasamos las imágenes
            MainScreen(
                imageUrls = imageUrls,  // Aquí se pasan las imágenes al carrusel
                onMapClick = {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                },
                onMuseumClick = {
                    val intent = Intent(this, MuseumsListActivity::class.java)
                    startActivity(intent)
                },
                onRecommendationClick = {
                    val intent = Intent(this, RecommendationsActivity::class.java)
                    startActivity(intent)
                },
                onBackClick = {
                    // No hacer nada cuando se presiona la flecha de retroceso
                }
            )
        }
    }
}
