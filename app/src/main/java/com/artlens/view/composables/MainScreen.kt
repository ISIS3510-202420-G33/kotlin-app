package com.artlens.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(onArtistDetailClick: () -> Unit, onArtworkDetailClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onArtistDetailClick,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Go to Artist Detail")
        }

        Button(
            onClick = onArtworkDetailClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Artwork Detail")
        }
    }
}
