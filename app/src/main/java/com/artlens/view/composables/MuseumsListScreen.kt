package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.artlens.R
import com.artlens.data.models.MuseumResponse
import com.artlens.utils.UserPreferences

@Composable
fun MuseumsListScreen(
    museums: List<MuseumResponse>,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCameraClick:() -> Unit,
    onRecommendationClick: () -> Unit,
    onUserClick: () -> Unit,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    logOutClick: () -> Unit,
    onViewFavoritesClick: () -> Unit,
    onMuseumClick: (Int) -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Add padding to the box
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
            // Barra superior
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
                    text = "MUSEUMS",
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

            // Grid de museos con dos columnas
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(museums) { museum ->
                    MuseumItem(museum = museum, onClick = { onMuseumClick(museum.pk) })
                }
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
                    contentDescription = "Recommendations",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun MuseumItem(museum: MuseumResponse, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        // Imagen del museo
        Image(
            painter = rememberImagePainter(data = museum.fields.image),
            contentDescription = museum.fields.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del museo
        Text(
            text = museum.fields.name,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )
    }
}
