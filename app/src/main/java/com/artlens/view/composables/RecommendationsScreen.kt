package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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

@Composable
fun RecommendationsScreen(
    onBackClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit // Aceptamos un callback con el ID de la obra
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con la flecha atrás y el título
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
                    text = "RECOMMENDATIONS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
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

            // Contenido scrolleable con las tarjetas de recomendaciones
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recommendations) { recommendation ->
                    RecommendationCard(
                        recommendation = recommendation,
                        onClick = {
                            // Al hacer clic en la tarjeta, llamamos al callback pasando un ID por defecto (1)
                            onRecommendationClick(1)
                        }
                    )
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
fun RecommendationCard(recommendation: Recommendation, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Tarjeta clicable
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }, // Hacemos la tarjeta clicable
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
                        .weight(0.4f) // Ocupa el 40% del ancho de la tarjeta
                        .height(120.dp)  // Ajuste para imagen rectangular
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Línea divisoria negra delgada debajo de la tarjeta
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

// Lista de recomendaciones
val recommendations = listOf(
    Recommendation(
        title = "Nighthawks",
        description = "A painting by Edward Hopper, depicting four people in an urban diner at night.",
        imageUrl = "https://th.bing.com/th/id/R.8979cc84bd3032a79edde4d6ea501807?rik=ay2ZGiXakdQEPQ&pid=ImgRaw&r=0"
    ),
    Recommendation(
        title = "The scream",
        description = "The famous painting by Norwegian artist Edvard Munch.",
        imageUrl = "https://th.bing.com/th/id/R.8979cc84bd3032a79edde4d6ea501807?rik=ay2ZGiXakdQEPQ&pid=ImgRaw&r=0"
    ),
    Recommendation(
        title = "Saturn",
        description = "Saturn Devouring His Son by Francisco Goya.",
        imageUrl = "https://th.bing.com/th/id/R.8979cc84bd3032a79edde4d6ea501807?rik=ay2ZGiXakdQEPQ&pid=ImgRaw&r=0"
    )
)
