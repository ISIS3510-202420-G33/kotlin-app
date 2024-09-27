package com.artlens.view.composables

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.artlens.data.models.ArtistResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@Composable
fun ArtistDetailScreen(artist: ArtistResponse?) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        artist?.let {
            // Texto del nombre del artista
            Text(text = it.fields.name, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))

            // Texto de la biografía del artista
            Text(text = it.fields.biography, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(16.dp))

            // Cargar la imagen con Glide
            AndroidView(
                factory = { ImageView(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                update = { imageView ->
                    Glide.with(context)
                        .load(it.fields.image)
                        .apply(RequestOptions().centerCrop())
                        .into(imageView)
                }
            )

        } ?: run {
            Text(text = "Artist not found", style = MaterialTheme.typography.body1)
        }
    }
}
