package com.example.artlens

import android.Manifest
import android.widget.ImageButton
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.artlens.R
import com.artlens.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtener el SupportMapFragment y preparar el mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Configurar el botón de regreso
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()  // Cierra la actividad actual y vuelve a la anterior
        }

        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            enableMyLocation()
        }

        // Añadir los marcadores de los museos
        addMuseumMarkers()
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            // Obtener la ubicación actual del usuario
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                }
            }
        }
    }

    private fun addMuseumMarkers() {
        // Museo Nacional de Colombia
        val museoNacional = LatLng(4.615551, -74.068818)
        mMap.addMarker(MarkerOptions().position(museoNacional).title("Museo Nacional de Colombia"))

        // Museo del Oro
        val museoDelOro = LatLng(4.605371, -74.070278)
        mMap.addMarker(MarkerOptions().position(museoDelOro).title("Museo del Oro"))

        // Museo Botero
        val museoBotero = LatLng(4.598250, -74.075624)
        mMap.addMarker(MarkerOptions().position(museoBotero).title("Museo Botero"))

        // Casa de Moneda
        val casaDeMoneda = LatLng(4.598900, -74.075668)
        mMap.addMarker(MarkerOptions().position(casaDeMoneda).title("Casa de Moneda"))

        // Museo de Arte Moderno de Bogotá (MAMBO)
        val mambo = LatLng(4.611277, -74.070438)
        mMap.addMarker(MarkerOptions().position(mambo).title("Museo de Arte Moderno de Bogotá (MAMBO)"))

        // Museo de Bogotá
        val museoDeBogota = LatLng(4.599825, -74.076156)
        mMap.addMarker(MarkerOptions().position(museoDeBogota).title("Museo de Bogotá"))

        // Museo Colonial
        val museoColonial = LatLng(4.602019, -74.071960)
        mMap.addMarker(MarkerOptions().position(museoColonial).title("Museo Colonial"))

        // Museo de la Esmeralda
        val museoEsmeralda = LatLng(4.601464, -74.068297)
        mMap.addMarker(MarkerOptions().position(museoEsmeralda).title("Museo de la Esmeralda"))

        // Museo Santa Clara
        val museoSantaClara = LatLng(4.597944, -74.072523)
        mMap.addMarker(MarkerOptions().position(museoSantaClara).title("Museo Santa Clara"))

        // Planetario de Bogotá
        val planetarioBogota = LatLng(4.609710, -74.070089)
        mMap.addMarker(MarkerOptions().position(planetarioBogota).title("Planetario de Bogotá"))
    }


    // Manejar la respuesta del permiso de ubicación
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }
}
