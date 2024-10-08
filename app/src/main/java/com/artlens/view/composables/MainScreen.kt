package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.artlens.R
import com.artlens.utils.UserPreferences

@Composable
fun MainScreen(
    imageUrls: List<String>,
    museumIds: List<Int>,
    onMapClick: () -> Unit,
    onMuseumClick: (Int) -> Unit,
    onRecommendationClick: () -> Unit,
    onArtistClick: () -> Unit,
    onBackClick: () -> Unit,
    onUserClick: () -> Unit,
    onCameraClick: () -> Unit,
    onMuseumsClick: () -> Unit,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    logOutClick: () -> Unit,
    onViewFavoritesClick: () -> Unit

) {

    if (showDialog) {
        
        AlertDialog(
            onDismissRequest = onDismissDialog,
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Hi ${UserPreferences.getUsername()}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )


                        Spacer(modifier = Modifier.height(8.dp))

                        // Horizontal Divider
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Black)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        // First Option Button
                        Button(
                            onClick = {
                                onDismissDialog()
                                onViewFavoritesClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text("View Favorites")
                        }

                        // Second Option Button
                        Button(
                            onClick = {
                                logOutClick()
                                onDismissDialog()}
                            ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text("Log Out")
                        }
                    }
                }
            }
        )
    }



    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
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
                IconButton(onClick = onUserClick) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

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

            Spacer(modifier = Modifier.height(30.dp))

            // Título centrado
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Museums in your city",
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Carrusel de imágenes con URLs e IDs del backend
            MuseumImageCarousel(imageUrls = imageUrls, onMuseumClick = onMuseumClick, museumIds = museumIds)



            // Botones de acción
            Spacer(modifier = Modifier.height(100.dp))
            Button(
                onClick = { onMuseumsClick() },
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
                onClick =  onArtistClick,
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
            IconButton(onClick = { /* Acción para ir a Home */ }) {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = onCameraClick) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Museos",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = onRecommendationClick) {
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
fun MuseumImageCarousel(imageUrls: List<String>, onMuseumClick: (Int) -> Unit, museumIds: List<Int>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        itemsIndexed(imageUrls) { index, imageUrl ->  // Usamos itemsIndexed para obtener el índice
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .width(310.dp)
                    .height(500.dp)
                    .clip(CircleShape)
                    .clickable {
                        // Al hacer clic, redirigimos al detalle del museo correspondiente
                        onMuseumClick(museumIds[index])
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}
