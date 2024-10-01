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
fun LikesScreen(
    onBackClick: () -> Unit,
    onArtworkClick: (Int) -> Unit,
    artworkListViewModel: ArtworkListViewModel  // Use this ViewModel to fetch liked artworks
) {
    // Fetch liked artworks from the ViewModel
    val likedArtworks by artworkListViewModel.likedArtworks.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Back Arrow",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    text = "LIKES",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // List of liked artworks
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(likedArtworks) { artwork ->
                    LikedArtworkCard(
                        artwork = artwork,
                        onArtworkClick = { onArtworkClick(artwork.pk) },
                        onRemoveLikeClick = {
                            artworkListViewModel.removeLike(artwork.pk)  // Remove like from ViewModel
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LikedArtworkCard(
    artwork: ArtworkResponse,
    onArtworkClick: () -> Unit,
    onRemoveLikeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArtworkClick() },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Artwork details
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = artwork.fields.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = artwork.fields.advancedInfo ?: "No description available",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Remove Like Button (Trash Icon)
            IconButton(onClick = onRemoveLikeClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trash),  // Assume you have a trash icon in drawables
                    contentDescription = "Remove Like",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
