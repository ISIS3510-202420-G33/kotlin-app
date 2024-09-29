package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.artlens.data.models.MuseumResponse

@Composable
fun MuseumDetailScreen(museum: MuseumResponse?) {
    Column(modifier = Modifier.padding(16.dp)) {
        museum?.let {
            Text(text = it.fields.name, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it.fields.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberImagePainter(data = it.fields.image),
                contentDescription = it.fields.name,
                modifier = Modifier.size(1000.dp),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Text(text = "Museum not found", style = MaterialTheme.typography.body1)
        }
    }
}
