package com.artlens.view.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.artlens.data.models.ArtworkResponse
import com.artlens.viewmodels.RecommendationsViewModel

@Composable
fun RecommendationsScreen(
    onBackClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit,
    recommendationsViewModel: RecommendationsViewModel, // Recibe el ViewModel como parámetro
    isLoggedIn: Boolean
) {
    // Obtenemos la obra más likeada y las recomendaciones desde el ViewModel
    val mostLikedArtwork by recommendationsViewModel.mostLikedArtwork.observeAsState()
    val recommendations by recommendationsViewModel.recommendations.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Hacemos que toda la pantalla sea scrolleable
        ) {
            // Barra superior con la flecha atrás y el título centrado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "RECOMMENDATIONS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar la obra más likeada si está disponible
            mostLikedArtwork?.let {
                Text(
                    text = "Most Liked Artwork",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, // Centramos el texto
                    modifier = Modifier.fillMaxWidth() // Aseguramos que el texto ocupe el ancho completo
                )
                Spacer(modifier = Modifier.height(8.dp))
                ArtworkCard(
                    artwork = it,
                    onClick = { onRecommendationClick(it.pk) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            } ?: run {
                Text(text = "Loading Most Liked Artwork...", fontSize = 16.sp, color = Color.Gray)
            }

            // Si el usuario está logueado, mostrar las recomendaciones
            if (isLoggedIn) {
                Text(
                    text = "Based on your likes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, // Centramos el texto
                    modifier = Modifier.fillMaxWidth() // Aseguramos que el texto ocupe el ancho completo
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar las recomendaciones
                recommendations.forEach { artwork ->
                    ArtworkCard(
                        artwork = artwork,
                        onClick = { onRecommendationClick(artwork.pk) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // Mostrar mensaje si el usuario no está logueado
                Text(
                    text = "Debes logearte para recibir recomendaciones personalizadas.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
                )
            }
        }
    }
}

@Composable
fun ArtworkCard(artwork: ArtworkResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto a la izquierda
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = artwork.fields.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = artwork.fields.interpretation,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Imagen rectangular a la derecha
            if (artwork.fields.image.isNotBlank()) {
                Image(
                    painter = rememberImagePainter(data = artwork.fields.image),
                    contentDescription = artwork.fields.name,
                    modifier = Modifier
                        .weight(0.4f)
                        .height(120.dp)  // Ajuste para imagen rectangular
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

