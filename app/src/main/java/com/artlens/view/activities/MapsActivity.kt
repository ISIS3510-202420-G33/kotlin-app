package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artlens.R
import com.artlens.view.activities.MainActivity
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.material.IconButton


class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapScreen()
        }
    }

    @Composable
    fun MapScreen() {
        val context = LocalContext.current

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
            GoogleMapComposable(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 80.dp)  // Ajustar la altura para dejar espacio a la barra superior
            )
        }
    }


    @Composable
    fun GoogleMapComposable(modifier: Modifier = Modifier) {
        val bogota = LatLng(4.60971, -74.08175)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(bogota, 12f)
        }

        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState
        ) {
            val museums = listOf(
                LatLng(4.601172, -74.066151), // Museo Nacional
                LatLng(4.610077, -74.070797), // Museo del Oro
                LatLng(4.597246, -74.076713), // Museo de Arte Moderno
                LatLng(4.609874, -74.073228), // Museo Botero
                LatLng(4.612463, -74.069598), // Casa Museo Quinta de Bolívar
                LatLng(4.601467, -74.064678), // Museo de Bogotá
                LatLng(4.614275, -74.070048), // Museo Colonial
                LatLng(4.610474, -74.064987), // Museo Arqueológico
                LatLng(4.613337, -74.068934), // Museo de Trajes
                LatLng(4.622398, -74.068244)  // Museo de los Niños
            )

            val museumNames = listOf(
                "Museo Nacional",
                "Museo del Oro",
                "Museo de Arte Moderno",
                "Museo Botero",
                "Casa Museo Quinta de Bolívar",
                "Museo de Bogotá",
                "Museo Colonial",
                "Museo Arqueológico",
                "Museo de Trajes",
                "Museo de los Niños"
            )

            museums.forEachIndexed { index, latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = museumNames[index]
                )
            }
        }
    }



}




