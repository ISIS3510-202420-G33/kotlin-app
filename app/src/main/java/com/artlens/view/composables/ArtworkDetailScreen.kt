package com.artlens.view.composables

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.artlens.R
import com.artlens.data.models.ArtworkResponse

@Composable
fun ArtworkDetailScreen(
    artwork: ArtworkResponse?,
    isLiked: Boolean,
    artistName: String?,
    isSpeaking: Boolean,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onMoreInfoClick: (Int) -> Unit,
    onInterpretationSpeakClick: (String) -> Unit,
    onCrashButtonClick: () -> Unit,
    onCommentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ARTWORK", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Back Arrow",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción de perfil */ }) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                backgroundColor = Color.White
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        // Contenedor desplazable para el contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Fila para la estrella de "like"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                val likedColor = Color(red = 160, green = 82, blue = 45)
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Like Artwork",
                        tint = if (isLiked) likedColor else Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            artwork?.let {
                // Nombre de la obra
                Text(
                    text = it.fields.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.fields.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = it.fields.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )


                Spacer(modifier = Modifier.height(16.dp))

                // Artista y Técnica
                Text(
                    text = "Artist: ${artistName ?: "Loading..."}",
                    style = MaterialTheme.typography.body1
                )
                Text(text = "Technique: ${it.fields.technique}", style = MaterialTheme.typography.body1)
                Text(text = "Interpretations: ${it.fields.interpretation}", style = MaterialTheme.typography.body1)

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para simular el crash
                Button(
                    onClick = { onCrashButtonClick() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Crash App Simulation")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ícono de audio dinámico
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            onInterpretationSpeakClick(it.fields.interpretation)
                        }
                    ) {
                        Icon(
                            painter = if (isSpeaking) {
                                painterResource(id = R.drawable.pause)
                            } else {
                                painterResource(id = R.drawable.headphones)
                            },
                            contentDescription = if (isSpeaking) "Pause Audio" else "Start Audio",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSpeaking) "Click the icon to stop the audio narration."
                        else "Click the icon to play the audio narration."
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para ir a la pantalla del artista
                Button(
                    onClick = { onMoreInfoClick(it.fields.artist) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("View Artist Detail")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para ir a la pantalla de comentarios
                Text(text = "FORUM", fontWeight = FontWeight.Bold)
                Button(
                    onClick = onCommentsClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("View Comments")
                }
            } ?: run {
                Text(text = "Artwork not found", style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
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

        IconButton(onClick = { /* Acción para ir a Museos */ }) {
            Image(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Museos",
                modifier = Modifier.size(30.dp)
            )
        }

        IconButton(onClick = { /* Acción para ir a Artistas */ }) {
            Image(
                painter = painterResource(id = R.drawable.fire),
                contentDescription = "Artistas",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
