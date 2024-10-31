package com.artlens.view.activities

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.artlens.view.composables.ArtistListScreen
import com.artlens.view.composables.NoInternetScreen
import com.artlens.view.viewmodels.ArtistListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.data.services.NetworkUtils
import com.artlens.data.services.NetworkReceiver
import android.net.ConnectivityManager
import androidx.compose.runtime.livedata.observeAsState

class ArtistListActivity : ComponentActivity() {

    // Obtenemos el ViewModel de la lista de artistas
    private val artistListViewModel: ArtistListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el BroadcastReceiver para monitorear la conexión a internet
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // Llamamos a la función para obtener todos los artistas
        artistListViewModel.fetchAllArtists()

        setContent {
            // Verificar el estado de conexión al iniciar
            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@ArtistListActivity)
            }

            if (isConnected) {
                // Observa los datos de la lista de artistas desde el ViewModel
                val artists = artistListViewModel.artistsLiveData.observeAsState(emptyList()).value

                // Pasamos la lista de artistas y las acciones a la pantalla de ArtistListScreen
                ArtistListScreen(
                    artists = artists,
                    onBackClick = { onBackPressed() },
                    onHomeClick = {
                        // Navegar a la pantalla de inicio
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    },
                    onRecommendationClick = {
                        // Navegar a la pantalla de recomendaciones
                        val intent = Intent(this, RecommendationsActivity::class.java)
                        startActivity(intent)
                    },
                    onArtistClick = { artistId ->
                        // Navegar a la pantalla de detalles del artista
                        val intent = Intent(this, ArtistDetailActivity::class.java)
                        intent.putExtra("ARTIST_ID", artistId)
                        startActivity(intent)
                    }
                )
            } else {
                // Mostrar pantalla de espera si no hay conexión
                NoInternetScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Desregistrar el BroadcastReceiver para evitar fugas de memoria
        unregisterReceiver(networkReceiver)
    }
}
