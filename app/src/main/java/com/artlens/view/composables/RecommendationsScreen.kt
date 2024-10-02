package com.artlens.view.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.artlens.view.viewmodels.ArtworkListViewModel

@Composable
fun RecommendationsScreen(
    onBackClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit,
    artworkListViewModel: ArtworkListViewModel // Recibe el ViewModel como parámetro
) {
    // Observa los datos desde el ViewModel
    val artworks by artworkListViewModel.artworksLiveData.observeAsState(emptyList())
    Log.d("RecommendationsScreen", "Number of artworks: ${artworks.size}")

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
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
                        text = "RECOMMENDATIONS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center // Asegura que el texto esté centrado
                    )
                }
            }

            // Lista de recomendaciones con los datos del backend
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(artworks.take(10)) { artwork ->  // Solo muestra las primeras 5 obras
                    RecommendationCard(
                        recommendation = Recommendation(
                            title = artwork.fields.name,
                            description = limitWords(artwork.fields.advancedInfo ?: "No description available", 20),
                            imageUrl = artwork.fields.image,
                        ),
                        onClick = {
                            onRecommendationClick(artwork.pk)  // Usa el ID real de la obra
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun RecommendationCard(recommendation: Recommendation, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.d("RecommendationCard", "Card clicked: ${recommendation.title}")
                    onClick() },  // Hacemos la tarjeta clicable
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
                        text = recommendation.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = recommendation.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Imagen rectangular a la derecha
                Image(
                    painter = rememberImagePainter(data = recommendation.imageUrl),
                    contentDescription = recommendation.title,
                    modifier = Modifier
                        .weight(0.4f)
                        .height(120.dp)  // Ajuste para imagen rectangular
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Línea divisoria debajo de la tarjeta
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

// Modelo de datos para las recomendaciones
data class Recommendation(
    val title: String,
    val description: String,
    val imageUrl: String
)

// Función auxiliar para limitar las palabras
fun limitWords(text: String, wordLimit: Int): String {
    val words = text.split(" ") // Dividimos el texto en palabras
    return if (words.size > wordLimit) {
        words.take(wordLimit).joinToString(" ") + "..." // Limitamos las palabras y añadimos "..."
    } else {
        text // Retorna el texto completo si es menor o igual al límite
    }
}
