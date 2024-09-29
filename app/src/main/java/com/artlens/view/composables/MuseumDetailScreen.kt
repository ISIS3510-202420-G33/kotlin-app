package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.artlens.R
import com.artlens.data.models.MuseumResponse

@Composable
fun MuseumDetailScreen(
    museum: MuseumResponse?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con flecha atrás y el título del museo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = rememberImagePainter(data = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Título del museo
                Text(
                    text = museum?.fields?.name ?: "MUSEUM",
                    fontSize = 24.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                IconButton(onClick = { /* Acción del perfil */ }) {
                    Image(
                        painter = rememberImagePainter(data = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // Imagen del museo
            Spacer(modifier = Modifier.height(16.dp))
            museum?.let {
                Image(
                    painter = rememberImagePainter(data = it.fields.image),
                    contentDescription = it.fields.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción del museo
                Text(text = it.fields.description, style = MaterialTheme.typography.body1)

                Spacer(modifier = Modifier.height(24.dp))

                // Carrusel de imágenes de "highlighted artworks"
                Text(text = "Highlighted Artworks", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                ImageArtCarousel(
                    imageUrls = listOf(
                        "https://th.bing.com/th/id/R.acf698c2cee6cc0847f46ab6f997dae5?rik=7BbDKY96cEdGBw&pid=ImgRaw&r=0",
                        "https://th.bing.com/th/id/R.acf698c2cee6cc0847f46ab6f997dae5?rik=7BbDKY96cEdGBw&pid=ImgRaw&r=0",
                        "https://th.bing.com/th/id/R.acf698c2cee6cc0847f46ab6f997dae5?rik=7BbDKY96cEdGBw&pid=ImgRaw&r=0"
                    )
                )
            } ?: run {
                Text(text = "Museum not found", style = MaterialTheme.typography.body1)
            }
        }

        // Barra de navegación inferior sobrepuesta
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp)
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHomeClick) {
                Image(
                    painter = rememberImagePainter(data = R.drawable.house),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = { /* Acción de cámara (Museos) */ }) {
                Image(
                    painter = rememberImagePainter(data = R.drawable.camera),
                    contentDescription = "Museos",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = { /* Acción de artistas */ }) {
                Image(
                    painter = rememberImagePainter(data = R.drawable.fire),
                    contentDescription = "Artistas",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun ImageArtCarousel(imageUrls: List<String>) {
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
                    .padding(2.dp)
                    .width(300.dp)
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Fit
            )
        }
    }
}

