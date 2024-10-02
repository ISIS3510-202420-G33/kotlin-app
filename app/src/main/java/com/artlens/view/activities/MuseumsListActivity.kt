package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.artlens.view.composables.MuseumsListScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.utils.UserPreferences

class MuseumsListActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            // Observar el estado de los museos desde el ViewModel
            var showDialog by remember { mutableStateOf(false) }
            val museumsState by museumsViewModel.museumsLiveData.observeAsState(emptyList()) // Observa la lista de museos

            // Pasar la lista de museos y el manejador del click a la pantalla de lista
            MuseumsListScreen(
                museums = museumsState,
                onBackClick = { onBackPressed() }, // Flecha de regreso
                onHomeClick = { navigateToMainActivity() }, // Botón de Home
                onRecommendationClick = { navigateToRecommendations() } ,
                onCameraClick = {
                    val intent = Intent(this, qrCodeActivity::class.java)
                    startActivity(intent)},
                onUserClick = {
                    val pk = UserPreferences.getPk()
                    if(pk!!>=0) {
                        showDialog = true
                    }
                    else{
                        val intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                    }},
                showDialog = showDialog,

                onDismissDialog = { showDialog = false },

                logOutClick = {UserPreferences.clearUserData()},

                onViewFavoritesClick = {
                    // Navegar a la pantalla de Likes (ListScreenActivity)
                    val intent = Intent(this, ListScreenActivity::class.java)
                    startActivity(intent)
                }

            ) { museumId ->
                // Crear un intent para ir a MuseumsDetailActivity con el ID del museo seleccionado
                val intent = Intent(this, MuseumsDetailActivity::class.java)
                intent.putExtra("MUSEUM_ID", museumId)
                startActivity(intent)
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRecommendations() {
        val intent = Intent(this, RecommendationsActivity::class.java)
        startActivity(intent)
    }
}
