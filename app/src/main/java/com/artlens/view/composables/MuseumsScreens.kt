package com.artlens.view.composables

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.rememberImagePainter
import com.artlens.R
import com.artlens.view.activities.MainActivity

@Composable
fun MuseumsScreen(onMapClick: () -> Unit, onMuseumClick: () -> Unit) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con iconos de regreso y perfil
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono de retroceso (flecha)
                IconButton(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent) // Regresar a MainActivity
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow"
                    )
                }

                // Título centrado
                Text(
                    text = "MUSEUMS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                // Icono de perfil (puede implementar funcionalidad si lo deseas)
                IconButton(
                    onClick = { /* Acción para abrir perfil */ },
                    modifier = Modifier.size(24.dp)
                ) {

                }
            }

            // Lista de museos en formato cuadrícula (scrolleable)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp), // Dejar espacio para los botones
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(museums) { museum ->
                    MuseumItem(museum)
                }
            }
        }

        // Barra de navegación inferior sobrepuesta
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 16.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón "Home"
            IconButton(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)  // Navegar a la MainActivity
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            }

            // Botón de "Cámara" (puedes implementar la acción)
            IconButton(
                onClick = { /* Acción para ir a Museos */ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Museos",
                    modifier = Modifier.size(30.dp)
                )
            }

            // Botón de "Fuego" (puedes implementar la acción)
            IconButton(
                onClick = { /* Acción para ir a Artistas */ }
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
fun MuseumItem(museum: Museum) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Imagen del museo
        Image(
            painter = rememberImagePainter(data = museum.imageUrl),
            contentDescription = museum.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del museo
        Text(
            text = museum.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Datos de prueba para los museos
val museums = listOf(
    Museum("Museo del Oro", "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain"),
    Museum("MOMA", "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain"),
    Museum("Florero de Llorente", "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain"),
    Museum("Louvre Museum", "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain"),
    Museum("Museo Nacional", "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain")
)

data class Museum(val name: String, val imageUrl: String)
