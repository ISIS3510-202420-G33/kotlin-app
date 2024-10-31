package com.artlens.view.activities

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.artlens.R
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.data.models.MuseumResponse
import com.artlens.view.composables.NoInternetScreen
import com.artlens.data.services.NetworkUtils
import com.artlens.data.services.NetworkReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import android.net.ConnectivityManager

class MapsActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private var userLat: Double = 4.60971 // Valor por defecto (Bogotá)
    private var userLng: Double = -74.08175 // Valor por defecto (Bogotá)

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            updateLocationAndMuseums()
        } else {
            Log.w("MapsActivity", "Permiso de ubicación denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Configurar el BroadcastReceiver para monitorear la conexión a internet
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContent {
            // Verificar el estado de conexión al iniciar
            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@MapsActivity)
            }

            if (isConnected) {
                MapScreen()
            } else {
                NoInternetScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    @Composable
    fun MapScreen() {
        val context = LocalContext.current
        var closestMuseums by remember { mutableStateOf(emptyList<MuseumResponse>()) }

        // Pedir permisos y actualizar ubicación y museos cada 10 segundos
        LaunchedEffect(Unit) {
            checkAndRequestLocationPermission(context)
            while (true) {
                updateLocationAndMuseums { updatedMuseums ->
                    closestMuseums = updatedMuseums
                }
                delay(10000) // Esperar 10 segundos antes de la próxima actualización
            }
        }

        // Pantalla del mapa
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                TopBar()
                GoogleMapComposable(userLat, userLng, closestMuseums)
            }
        }
    }

    private fun checkAndRequestLocationPermission(context: android.content.Context) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                updateLocationAndMuseums { }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Actualizar ubicación y lista de museos cercanos
    private fun updateLocationAndMuseums(onMuseumsUpdated: (List<MuseumResponse>) -> Unit = {}) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLat = it.latitude
                    userLng = it.longitude
                    museumsViewModel.getClosestMuseums(userLat, userLng).observe(this) { museums ->
                        onMuseumsUpdated(museums)
                    }
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onBackPressed() }) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    text = "HOME",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {}) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun GoogleMapComposable(userLat: Double, userLng: Double, museums: List<MuseumResponse>) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(userLat, userLng), 12f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true)
        ) {
            museums.forEach { museum ->
                val museumLocation = LatLng(museum.fields.latitude, museum.fields.longitude)
                Marker(
                    state = MarkerState(position = museumLocation),
                    title = museum.fields.name
                )
            }
        }
    }
}
