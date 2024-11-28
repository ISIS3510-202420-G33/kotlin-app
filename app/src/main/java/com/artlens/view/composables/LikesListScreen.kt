package com.artlens.view.composables

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.artlens.R
import com.artlens.view.viewmodels.LikesViewModel
import androidx.compose.ui.platform.LocalContext
import com.artlens.data.models.ArtworkResponse

@Composable
fun LikesListScreen(
    onBackClick: () -> Unit,
    onMuseumClick: (Int) -> Unit,
    onDownloadClick: (List<ArtworkResponse>) -> Unit,
    likesViewModel: LikesViewModel
) {
    val context = LocalContext.current
    val likedMuseums by likesViewModel.likedMuseums.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        NoInternetDialog(
            onDismiss = {  showDialog = false }  // Close the dialog
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con la flecha atrás y el título centrado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flecha de retroceso alineada a la izquierda
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Caja que llena el espacio restante y centra el texto
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "LIKES",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            //Boton para descargar info
            Button(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,   // Button background color
                    contentColor = Color.White       // Text color inside the button
                ),
                onClick = {
                    onDownloadClick(likedMuseums)
                }
            ) {
                Text("Download Favorites")
            }


            // Lista de museos que el usuario ha marcado como "Me gusta"
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(likedMuseums) { artwork ->
                    MuseumLikeCard(
                        museum = Recommendations(
                            title = artwork.fields.name,
                            description = limitWordss(artwork.fields.advancedInfo ?: "No description available", 20),
                            imageUrl = artwork.fields.image
                        ),
                        onClick = {
                            if (isInternetAvailable(context)) {
                                onMuseumClick(artwork.pk)
                            } else {
                                showDialog = true // Show the dialog when no internet is available
                            }
                            },

                        onRemoveLike = { likesViewModel.removeLike(artwork.pk) },
                        setFalse = {showDialog = true}
                    )
                }
            }
        }
    }
}

@Composable
fun MuseumLikeCard(
    museum: Recommendations,
    onClick: () -> Unit,
    onRemoveLike: () -> Unit,
    setFalse: () -> Unit,

) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Columna para el texto (título y descripción)
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(end = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Título de la obra
                        Text(
                            text = museum.title,
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        // Ícono de basura
                        IconButton(
                            onClick = {

                                if (isInternetAvailable(context)) {
                                    onRemoveLike()
                                } else {
                                    setFalse() // Show the dialog when no internet is available
                                }

                                      },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.trash),
                                contentDescription = "Remove Like",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Descripción de la obra
                    Text(
                        text = museum.description,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }

                // Imagen del museo a la derecha
                Image(
                    painter = rememberAsyncImagePainter(model = museum.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.4f)
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}




// Modelo de datos para la tarjeta
data class Recommendations(
    val title: String,
    val description: String,
    val imageUrl: String
)

// Función auxiliar para limitar las palabras
fun limitWordss(text: String, wordLimit: Int): String {
    val words = text.split(" ")
    return if (words.size > wordLimit) {
        words.take(wordLimit).joinToString(" ") + "..."
    } else {
        text
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
