package com.artlens.view.composables

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.artlens.R

// Agregar el parámetro `onMapClick` y `onMuseumClick`
@Composable
fun MainScreen(
    onMapClick: () -> Unit,   // Callback para abrir el mapa
    onMuseumClick: () -> Unit // Callback para abrir la lista de museos
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Contenido principal con peso para ocupar el espacio disponible
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Welcome to Artlens!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Barra de búsqueda simulada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(Color(0xFFE0E0E0), shape = CircleShape)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Find a piece of art, an artist or a museum",
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                // Carrusel de imágenes
                Spacer(modifier = Modifier.height(100.dp))
                ImageCarousel(
                    imageUrls = listOf(
                        "https://th.bing.com/th/id/R.ac9e679812389a4b4abb15c8cf4705ee?rik=7Gp6HUNIzI7AxA&riu=http%3A%2F%2Fmedia.architecturaldigest.com%2Fphotos%2F5900cc370638dd3b70018b33%2Fmaster%2Fpass%2FSecrets+of+Louvre+1.jpg&ehk=hAKFCm8l7I4rrpUDUohq%2BqtJ%2F%2B4bovQlQM2lH3C4fCk%3D&risl=&pid=ImgRaw&r=0",
                        "https://th.bing.com/th/id/R.f53b1a450649b368e2ab3f84efea6000?rik=5PTXwoJnk%2BwbvQ&pid=ImgRaw&r=0",
                        "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain"
                    )
                )

                // Botones de acción
                Spacer(modifier = Modifier.height(150.dp))
                Button(
                    onClick = onMuseumClick, // Acción al hacer click en "View Museums"
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("View Museums")
                }

                // Botón que abre el mapa, usando el `onMapClick`
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onMapClick, // Acción para abrir el mapa
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White // Para hacer el texto blanco
                    )
                ) {
                    Text("View Map")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Acción para ver artistas */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("View Artists")
                }
            }
        }

        // Barra de navegación inferior sobrepuesta (igual que en MuseumsScreen)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp)
                .height(50.dp),  // Tamaño ajustado de la barra inferior
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Acción para ir a Home */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp) // Tamaño ajustado de los íconos
                )
            }

            IconButton(
                onClick = { /* Acción para ir a Museos */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Museos",
                    modifier = Modifier.size(30.dp) // Tamaño ajustado de los íconos
                )
            }

            IconButton(
                onClick = { /* Acción para ir a Artistas */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fire),
                    contentDescription = "Artistas",
                    modifier = Modifier.size(30.dp) // Tamaño ajustado de los íconos
                )
            }
        }
    }
}

@Composable
fun ImageCarousel(imageUrls: List<String>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(imageUrls) { imageUrl ->
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}
