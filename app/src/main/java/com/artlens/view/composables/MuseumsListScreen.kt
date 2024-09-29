package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.artlens.data.models.MuseumResponse

@Composable
fun MuseumsListScreen(museums: List<MuseumResponse>, onMuseumClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(museums) { museum ->
            MuseumItem(museum = museum, onClick = { onMuseumClick(museum.pk) })
        }
    }
}

@Composable
fun MuseumItem(museum: MuseumResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick) // Hacer clic en el museo
    ) {
        // Cargar imagen del museo con Coil
        Image(
            painter = rememberImagePainter(data = museum.fields.image),
            contentDescription = museum.fields.name,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Mostrar nombre del museo
        Text(text = museum.fields.name, modifier = Modifier.weight(1f))
    }
}
