package com.artlens.view.composables

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.artlens.R
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@Composable
fun ArtistDetailScreen(
    artist: ArtistResponse?,
    artworks: List<ArtworkResponse>,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onArtworkClick: (Int) -> Unit  // Callback para manejar los clics en las obras de arte
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())  // Hacemos que la vista sea scrolleable
        ) {
            // Barra superior con flecha atrás y el título del artista
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colors.background),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Título centrado
                Text(
                    text = "ARTIST",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Icono de perfil a la derecha
                IconButton(onClick = { /* Acción de perfil */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Título centrado
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = artist?.fields?.name ?: "ARTIST",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(26.dp))
            artist?.let {
                // Imagen del artista en forma circular
                AndroidView(
                    factory = { ImageView(context) },
                    modifier = Modifier
                        .size(350.dp)
                        .clip(CircleShape)  // Redondeamos la imagen en forma de círculo
                        .align(Alignment.CenterHorizontally),
                    update = { imageView ->
                        Glide.with(context)
                            .load(it.fields.image)
                            .apply(RequestOptions().circleCrop())  // Glide también redondea la imagen
                            .into(imageView)
                    }
                )

                Spacer(modifier = Modifier.height(26.dp))

                // Mostrar la biografía del artista
                Text(
                    text = it.fields.biography,
                    style = MaterialTheme.typography.body1,
                    fontSize = 20.sp
                )
            } ?: run {
                Text(text = "Artist not found", style = MaterialTheme.typography.body1)
            }

            // Carrusel de obras de arte del artista
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Highlighted Artworks",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            // Carrusel de imágenes de obras de arte
            ArtworkCarousel(artworks = artworks, onArtworkClick = onArtworkClick)
            Spacer(modifier = Modifier.height(104.dp))
        }

        // Barra de navegación inferior
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
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = { /* Acción para ir a Museos */ }) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Museos",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = { /* Acción para ir a Recomendaciones */ }) {
                Image(
                    painter = painterResource(id = R.drawable.fire),
                    contentDescription = "Recommendations",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun ArtworkCarousel(artworks: List<ArtworkResponse>, onArtworkClick: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(artworks.take(5)) { artwork ->
            Box(modifier = Modifier.padding(8.dp)) {
                Image(
                    painter = rememberImagePainter(data = artwork.fields.image),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(3f / 4f)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { onArtworkClick(artwork.pk) },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


