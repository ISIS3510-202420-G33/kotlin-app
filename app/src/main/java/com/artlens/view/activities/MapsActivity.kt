package com.artlens.view.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.core.app.ActivityCompat
import com.artlens.R
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.data.models.MuseumResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

class MapsActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "Funcionalidad" to "Fun3",
            "Fecha" to Timestamp.now()
        )

        // Add a new document with a generated ID
        db.collection("BQ33")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        // Inicializamos el cliente para obtener la ubicación del usuario
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            // Contenedor del contenido
            MapScreen()
        }
    }

    @Composable
    fun MapScreen() {
        val context = LocalContext.current
        var userLat by remember { mutableStateOf(4.60971) }  // Bogotá como valor por defecto
        var userLng by remember { mutableStateOf(-74.08175) }

        // Obtenemos la ubicación del usuario cuando el Composable se inicializa
        LaunchedEffect(Unit) {
            if (ActivityCompat.checkSelfPermission(
                    context,
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

        // Obtenemos los museos más cercanos observando el LiveData
        val closestMuseumsLiveData = museumsViewModel.getClosestMuseums(userLat, userLng)
        val closestMuseums by closestMuseumsLiveData.observeAsState(emptyList())  // Observa los cambios

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Barra superior (similar a la de MainScreen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)  // Aproximadamente 1/8 de la altura de la pantalla
                    .background(Color.White)
            ) {
                // Flecha de retroceso a la izquierda
                IconButton(
                    onClick = {
                        // Acción para volver a la MainActivity
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart) // Alinear a la izquierda
                        .padding(start = 16.dp) // Añadir padding opcional
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)  // Tamaño ajustado de la flecha
                    )
                }

                // Título centrado
                Text(
                    text = "Map of Museums",  // Título centrado de la barra
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center) // Alineación centrada en el Box
                )
            }

            // Mapa ocupando el resto del espacio
            GoogleMapComposable(userLat, userLng, closestMuseums)
        }
    }

    @Composable
    fun GoogleMapComposable(userLat: Double, userLng: Double, museums: List<MuseumResponse>) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(userLat, userLng), 12f)
        }

        val context = LocalContext.current

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 80.dp),  // Ajustar la altura para dejar espacio a la barra superior
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                tiltGesturesEnabled = false // Desactivar el gesto de inclinación
            ),
            properties = MapProperties(
                isMyLocationEnabled = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED,
                mapType = MapType.NORMAL
            )
        )
        {
            // Solo mostramos los 5 museos más cercanos
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
