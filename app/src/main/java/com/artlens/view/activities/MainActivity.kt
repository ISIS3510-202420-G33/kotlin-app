package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.view.composables.MainScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private val db = Firebase.firestore // Inicializa Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Mostrar contenido principal si hay conexión
            val museums by museumsViewModel.museumsLiveData.observeAsState(emptyList())
            val imageUrls = museums?.map { it.fields.image }
            val museumIds = museums?.map { it.pk }

            var showDialog by remember { mutableStateOf(false) }

            if (imageUrls != null && museumIds != null) {
                MainScreen(
                    imageUrls = imageUrls,
                    showDialog = showDialog,
                    museumIds = museumIds,
                    onMapClick = {
                        val intent = Intent(this, MapsActivity::class.java)
                        startActivity(intent)
                    },
                    onMuseumClick = { museumId ->
                        logCarouselInteraction(museumId) // Registrar clic en la imagen
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
                    },
                    onSearchClick = {
                        val intent = Intent(this, SearchActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    // Función para registrar las interacciones con el carrusel
    private fun logCarouselInteraction(museumId: Int) {
        val userId = UserPreferences.getPk() ?: -1 // Obtiene el ID del usuario, si está disponible
        val interactionData = hashMapOf(
            "museumId" to museumId,
            "timestamp" to Timestamp.now(),
            "userId" to userId
        )

        db.collection("BQ330") // Cambia la colección a "3.30"
            .add(interactionData)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity", "Interacción registrada con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error registrando interacción", e)
            }
    }
}
