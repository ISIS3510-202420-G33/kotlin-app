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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.artlens.R
import com.artlens.data.models.ArtworkResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@Composable
fun ForumEntry(userName: String, comment: String) {
    Column {
        Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(
            text = comment,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 8.dp)
        )
        Divider(color = Color.Black, thickness = 1.dp)
    }
}


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
    modifier: Modifier = Modifier // Nuevo parámetro para el modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val context = LocalContext.current

        // Color personalizado para la estrella de "like"
        val likedColor = Color(red = 160, green = 82, blue = 45)

        var newComment by remember { mutableStateOf("") }
        var comments by remember {
            mutableStateOf(
                listOf(
                    "Santi2001: Nice artwork!!",
                    "ArtLover287: Overrated :c",
                    "Usuario123: Give us your opinion"
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Barra superior con la flecha atrás y el título centrado
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
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    text = "ARTWORK",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                IconButton(onClick = { /* Acción de perfil */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // Fila para la estrella de "like"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
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

                // Imagen de la obra
                AndroidView(
                    factory = { ImageView(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    update = { imageView ->
                        Glide.with(context)
                            .load(it.fields.image)
                            .apply(RequestOptions().centerInside())
                            .into(imageView)
                    }
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

                // Botón para simular el crash, centrado en la parte superior
                Button(
                    onClick = { onCrashButtonClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Crash App Simulation")
                }

                // Ícono de audio dinámico (Headphones o Pause)
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

                // Foro de opiniones
                Text(text = "FORUM", fontWeight = FontWeight.Bold)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    comments.forEach { comment ->
                        ForumEntry(
                            userName = comment.substringBefore(":"),
                            comment = comment.substringAfter(":")
                        )
                    }

                    // Campo para agregar nuevo comentario
                    TextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Add your comment...") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            if (newComment.isNotBlank()) {
                                comments = comments + "NewUser: $newComment"
                                newComment = ""
                            }
                        })
                    )
                }
            } ?: run {
                Text(text = "Artwork not found", style = MaterialTheme.typography.body1)
            }
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
}
