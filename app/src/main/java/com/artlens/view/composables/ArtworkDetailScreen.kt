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
import androidx.compose.ui.viewinterop.AndroidView
import com.artlens.R
import com.artlens.data.models.ArtworkResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@Composable
fun ArtworkDetailScreen(
    artwork: ArtworkResponse?,
    onBackClick: () -> Unit,
    onMoreInfoClick: (Int) -> Unit
) {
    val context = LocalContext.current
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

    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "ARTWORK",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Icono de perfil
                IconButton(onClick = { /* Acción de perfil */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // Espacio debajo de la barra superior
                horizontalArrangement = Arrangement.End // Alineamos el contenido a la derecha
            ) {
                IconButton(onClick = { /* Acción de estrella */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Star Icon",
                        modifier = Modifier.size(40.dp) // Tamaño del icono
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
                        .height(250.dp), // Redimensionamos para que no se corte
                    update = { imageView ->
                        Glide.with(context)
                            .load(it.fields.image)
                            .apply(RequestOptions().centerInside()) // Ajustamos el tamaño sin cortar la imagen
                            .into(imageView)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Artista y Técnica
                Text(text = "Artist: ${it.fields.name}", style = MaterialTheme.typography.body1)
                Text(text = "Technique: ${it.fields.technique}", style = MaterialTheme.typography.body1)


                // Interpretación
                Text(text = "Interpretations: ${it.fields.interpretation}", style = MaterialTheme.typography.body1)
                Text(
                    text = "Advance Information: ${it.fields.advancedInfo}", style = MaterialTheme.typography.body1)

                Spacer(modifier = Modifier.height(16.dp))

                // Ícono de audio y su descripción
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.headphones),
                        contentDescription = "Audio",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Click the icon to start the audio narration.")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de ver más detalles
                Button(
                    onClick = { onMoreInfoClick(it.fields.artist) }, // Pasa el ID del artista
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
                        ForumEntry(userName = comment.split(":")[0], comment = comment.split(":")[1])
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

@Composable
fun ForumEntry(userName: String, comment: String) {
    Column {
        Text(text = userName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        TextField(
            value = comment,
            onValueChange = { /* Solo lectura */ },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        Divider(color = Color.Black, thickness = 1.dp)
    }
}
