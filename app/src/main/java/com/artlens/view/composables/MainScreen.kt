package com.artlens.view.composables

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
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

// Agregar el parámetro `onMapClick`
@Composable
fun MainScreen(onMapClick: () -> Unit) {
    val context = LocalContext.current

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
            Spacer(modifier = Modifier.height(16.dp))
            ImageCarousel(
                imageUrls = listOf(
                    "https://th.bing.com/th/id/R.ac9e679812389a4b4abb15c8cf4705ee?rik=7Gp6HUNIzI7AxA&riu=http%3A%2F%2Fmedia.architecturaldigest.com%2Fphotos%2F5900cc370638dd3b70018b33%2Fmaster%2Fpass%2FSecrets+of+Louvre+1.jpg&ehk=hAKFCm8l7I4rrpUDUohq%2BqtJ%2F%2B4bovQlQM2lH3C4fCk%3D&risl=&pid=ImgRaw&r=0",
                    "https://th.bing.com/th/id/R.f53b1a450649b368e2ab3f84efea6000?rik=5PTXwoJnk%2BwbvQ&pid=ImgRaw&r=0",
                    "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain"
                )
            )

            // Título debajo del carrusel
            Text(
                text = "Museums in your city",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Botón que abre el mapa, usando el `onMapClick`
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onMapClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Map")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Acción para ver artistas */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Artists")
            }
        }

        // Barra de navegación inferior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(iconRes = R.drawable.house, onClick = { /* Acción para ir a Home */ })
            IconButton(iconRes = R.drawable.camera, onClick = { /* Acción para ir a Museos */ })
            IconButton(iconRes = R.drawable.fire, onClick = { /* Acción para ir a Artistas */ })
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

@Composable
fun IconButton(iconRes: Int, onClick: () -> Unit) {
    androidx.compose.material.IconButton(onClick = onClick) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}
