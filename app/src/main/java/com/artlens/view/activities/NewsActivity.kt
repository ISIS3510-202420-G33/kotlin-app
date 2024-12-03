package com.artlens.view.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.services.MockNews
import com.artlens.data.services.NetworkReceiver
import com.artlens.data.services.NetworkUtils
import com.artlens.view.composables.NewsScreen
import com.artlens.view.composables.NoInternetScreen

class NewsActivity : ComponentActivity() {

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar el BroadcastReceiver para monitorear la conexión a internet
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContent {
            // Verificar la conexión inicial
            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@NewsActivity)
            }

            if (isConnected) {
                // Mostrar pantalla de noticias si hay conexión
                NewsScreen(
                    newsList = MockNews.newsList,
                    onBackClick = { onBackPressed() },
                    onHomeClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    },
                    onRecommendationClick = {
                        val intent = Intent(this, RecommendationsActivity::class.java)
                        startActivity(intent)
                    },
                    onCameraClick = {
                        val intent = Intent(this, qrCodeActivity::class.java)
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
