package com.artlens.view.composables
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
import com.artlens.R
import coil.compose.rememberImagePainter

@Composable
fun MainScreen(
    onMapClick: () -> Unit,
    onMuseumClick: () -> Unit,
    onRecommendationClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con la flecha atrás y un título centrado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Flecha de retroceso
                IconButton(onClick = onBackClick) {
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
                    fontWeight = FontWeight.Bold
                )

                // Icono de perfil a la derecha
                IconButton(onClick = { /* Acción para abrir el perfil */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // Contenido principal
            Spacer(modifier = Modifier.height(16.dp))
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

            Spacer(modifier = Modifier.height(100.dp))
            ImageCarousel(
                imageUrls = listOf(
                    "https://c8.alamy.com/comp/DTR5X3/gold-museum-museo-del-oro-in-bogota-columbia-DTR5X3.jpg",
                    "https://c8.alamy.com/comp/DTR5X3/gold-museum-museo-del-oro-in-bogota-columbia-DTR5X3.jpg",
                    "https://c8.alamy.com/comp/DTR5X3/gold-museum-museo-del-oro-in-bogota-columbia-DTR5X3.jpg"
                )
            )

            // Botones de acción
            Spacer(modifier = Modifier.height(150.dp))
            Button(
                onClick = onMuseumClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("View Museums")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onMapClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
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
            IconButton(
                onClick = { /* Acción para ir a Home */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = { /* Acción para ir a Museos */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Museos",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = onRecommendationClick
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fire),
                    contentDescription = "Artistas",
                    modifier = Modifier.size(30.dp)
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
