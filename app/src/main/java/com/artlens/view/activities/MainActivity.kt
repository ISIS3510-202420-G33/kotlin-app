package com.artlens.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.artlens.view.composables.MainScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLat: Double? = null
    private var userLng: Double? = null

    // Registrar el resultado de la solicitud de permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            preLoadMap()  // Volver a intentar la precarga del mapa
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Verificar permisos y pre-cargar el mapa en segundo plano
        checkAndRequestLocationPermission()

        setContent {
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
                    userLat?.let {
                        intent.putExtra("latitude", it)
                    }
                    userLng?.let {
                        intent.putExtra("longitude", it)
                    }
                    intent.putExtra("zoom", 12f)  // Enviar también el nivel de zoom
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
                onBackClick = {},
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
        }
    }

    // Verificar y solicitar permisos de ubicación
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
        // Obtenemos la ubicación actual del usuario
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
