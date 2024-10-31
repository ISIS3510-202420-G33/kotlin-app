package com.artlens.view.activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.artlens.view.composables.MainScreen
import com.artlens.view.composables.NoInternetScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.artlens.data.services.NetworkUtils
import com.artlens.data.services.NetworkReceiver
import androidx.compose.runtime.livedata.observeAsState


class MainActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLat: Double? = null
    private var userLng: Double? = null
    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            preLoadMap()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkAndRequestLocationPermission()

        // Inicializar el BroadcastReceiver para monitorear la conexión a internet
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContent {
            // Comprobar el estado de conexión cuando se carga la pantalla
            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@MainActivity)
            }

            if (isConnected) {
                // Mostrar contenido principal si hay conexión
                val museums by museumsViewModel.museumsLiveData.observeAsState(emptyList())
                val imageUrls = museums.map { it.fields.image }
                val museumIds = museums.map { it.pk }

                var showDialog by remember { mutableStateOf(false) }

                MainScreen(
                    imageUrls = imageUrls,
                    showDialog = showDialog,
                    museumIds = museumIds,
                    onMapClick = {
                        val intent = Intent(this, MapsActivity::class.java)
                        userLat?.let { intent.putExtra("latitude", it) }
                        userLng?.let { intent.putExtra("longitude", it) }
                        intent.putExtra("zoom", 12f)
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
                        if (pk!! >= 0) {
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

    private fun checkAndRequestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                preLoadMap()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun preLoadMap() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLat = it.latitude
                    userLng = it.longitude
                }
            }
        }
    }
}
