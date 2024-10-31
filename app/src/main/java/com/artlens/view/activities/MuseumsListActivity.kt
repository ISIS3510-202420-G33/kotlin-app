package com.artlens.view.activities

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.artlens.view.composables.MuseumsListScreen
import com.artlens.view.composables.NoInternetScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.artlens.data.services.NetworkUtils
import com.artlens.data.services.NetworkReceiver
import android.net.ConnectivityManager
import androidx.compose.runtime.livedata.observeAsState

class MuseumsListActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el BroadcastReceiver para monitorear la conexión a internet
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContent {
            // Verificar el estado inicial de la conexión cuando se carga la pantalla
            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@MuseumsListActivity)
            }

            if (isConnected) {
                // Mostrar contenido principal si hay conexión
                var showDialog by remember { mutableStateOf(false) }
                val museumsState by museumsViewModel.museumsLiveData.observeAsState(emptyList())

                MuseumsListScreen(
                    museums = museumsState,
                    onBackClick = { onBackPressed() },
                    onHomeClick = { navigateToMainActivity() },
                    onRecommendationClick = { navigateToRecommendations() },
                    onCameraClick = {
                        val intent = Intent(this, qrCodeActivity::class.java)
                        startActivity(intent)
                    },
                    onUserClick = {
                        val pk = UserPreferences.getPk()
                        if (pk!! >= 0) {
                            showDialog = true
                        } else {
                            val intent = Intent(this, LogInActivity::class.java)
                            startActivity(intent)
                        }
                    },
                    showDialog = showDialog,
                    onDismissDialog = { showDialog = false },
                    logOutClick = { UserPreferences.clearUserData() },
                    onViewFavoritesClick = {
                        val intent = Intent(this, ListScreenActivity::class.java)
                        startActivity(intent)
                    }
                ) { museumId ->
                    val intent = Intent(this, MuseumsDetailActivity::class.java)
                    intent.putExtra("MUSEUM_ID", museumId)
                    startActivity(intent)
                }
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

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRecommendations() {
        val intent = Intent(this, RecommendationsActivity::class.java)
        startActivity(intent)
    }
}
