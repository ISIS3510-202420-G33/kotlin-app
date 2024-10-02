package com.artlens.view.activities

import android.Manifest
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

class MapsActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private var userLat: Double = 4.60971 // Valor por defecto (Bogotá)
    private var userLng: Double = -74.08175 // Valor por defecto (Bogotá)

    // Registrar la solicitud de permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLastLocation()  // Obtener ubicación si el permiso es concedido
        } else {
            Log.w("MapsActivity", "Permiso de ubicación denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Comenzar el mapa
        setContent {
            MapScreen()
        }
    }

    @Composable
    fun MapScreen() {
        val context = LocalContext.current

        // Pedir permisos al inicializar el Composable
        LaunchedEffect(Unit) {
            checkAndRequestLocationPermission(context)
        }

        // Obtener los museos cercanos basados en la ubicación actual
        val closestMuseums by museumsViewModel.getClosestMuseums(userLat, userLng).observeAsState(emptyList())

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
                // El permiso ya está concedido
                getLastLocation()
            }
            else -> {
                // Solicitar el permiso
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Obtener la última ubicación conocida del usuario
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLat = it.latitude
                    userLng = it.longitude
                    // Actualizar los museos cercanos una vez que se obtiene la ubicación
                    museumsViewModel.getClosestMuseums(userLat, userLng)
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
                // Flecha de retroceso
                IconButton(onClick = { onBackPressed() }) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Título centrado
                Text(
                    text = "HOME",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)

                // Icono de perfil a la derecha
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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
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
