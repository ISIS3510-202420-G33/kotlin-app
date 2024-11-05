package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.artlens.view.composables.MainScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.utils.UserPreferences
import androidx.compose.runtime.livedata.observeAsState

class MainActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

                // Mostrar contenido principal si hay conexión
                val museums by museumsViewModel.museumsLiveData.observeAsState(emptyList())
                val imageUrls = museums?.map { it.fields.image }
                val museumIds = museums?.map { it.pk }

                var showDialog by remember { mutableStateOf(false) }

            if (imageUrls != null) {
                if (museumIds != null) {
                    MainScreen(
                        imageUrls = imageUrls,
                        showDialog = showDialog,
                        museumIds = museumIds,
                        onMapClick = {
                            val intent = Intent(this, MapsActivity::class.java)
                            startActivity(intent)
                        },
                        onMuseumClick = { museumId ->
                            val intent = Intent(this, MuseumsDetailActivity::class.java)
                            intent.putExtra("MUSEUM_ID", museumId)
                            startActivity(intent)
                        },
                        onMuseumsClick = {
                            val intent = Intent(this, MuseumsListActivity::class.java)
                            startActivity(intent)
                        },
                        onArtistClick = {
                            val intent = Intent(this, ArtistListActivity::class.java)
                            startActivity(intent)
                        },
                        onRecommendationClick = {
                            val intent = Intent(this, RecommendationsActivity::class.java)
                            startActivity(intent)
                        },
                        onBackClick = {
                            // Aquí puedes manejar el comportamiento de retroceso si es necesario
                        },
                        onCameraClick = {
                            val intent = Intent(this, qrCodeActivity::class.java)
                            startActivity(intent)
                        },
                        onDismissDialog = { showDialog = false },
                        logOutClick = { UserPreferences.clearUserData() },
                        onUserClick = {
                            val pk = UserPreferences.getPk()
                            if (pk != null && pk >= 0) {
                                showDialog = true
                            } else {
                                val intent = Intent(this, LogInActivity::class.java)
                                startActivity(intent)
                            }
                        },
                        onViewFavoritesClick = {
                            val intent = Intent(this, ListScreenActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
            }
    }
}
