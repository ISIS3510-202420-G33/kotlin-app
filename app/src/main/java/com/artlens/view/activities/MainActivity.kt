package com.artlens.view.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.artlens.view.composables.MainScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.artlens.view.activities.MuseumsDetailActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Observar el estado de los museos
            val museums by museumsViewModel.museumsLiveData.observeAsState(emptyList())

            // Extraer las URLs de las imágenes y los IDs de los museos
            val imageUrls = museums.map { it.fields.image }
            val museumIds = museums.map { it.pk }  // Extraer los IDs de los museos

            var showDialog by remember { mutableStateOf(false) }

            MainScreen(
                imageUrls = imageUrls,
                showDialog = showDialog,
                museumIds = museumIds,  // Pasar los IDs de los museos al carrusel
                onMapClick = {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                },
                onMuseumClick = { museumId ->
                    // Redirigir a la pantalla de detalles del museo seleccionado
                    val intent = Intent(this, MuseumsDetailActivity::class.java)
                    intent.putExtra("MUSEUM_ID", museumId)
                    startActivity(intent)
                },
                onMuseumsClick = {
                    // Regresar a la lista de museos
                    val intent = Intent(this, MuseumsListActivity::class.java)
                    startActivity(intent)
                },
                onArtistClick = {
                    // Regresar a la lista de museos
                    val intent = Intent(this, ArtistDetailActivity::class.java)
                    startActivity(intent)
                },
                onRecommendationClick = {
                    val intent = Intent(this, RecommendationsActivity::class.java)
                    startActivity(intent)
                },
                onBackClick = {
                    // No hacer nada cuando se presiona la flecha de retroceso
                },

                onCameraClick = {

                    val intent = Intent(this, qrCodeActivity::class.java)
                    startActivity(intent)

                },

                onDismissDialog = { showDialog = false },

                logOutClick = {UserPreferences.clearUserData()},

                onUserClick = {


                    val pk = UserPreferences.getPk()

                    if(pk!!>=0) {

                        showDialog = true

                    }
                    else{

                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)

                    }
                }
            )
        }

    }
}
