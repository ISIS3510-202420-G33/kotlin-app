package com.artlens.view.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import coil.compose.rememberAsyncImagePainter
import com.artlens.R
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.view.viewmodels.LikesViewModel

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onUserClick: () -> Unit,
    onMuseumClick: (Int) -> Unit,
    onArtistClick: (Int) -> Unit,
    onArtworkClick: (Int) -> Unit,
    museums: List<MuseumResponse>,
    artists: List<ArtistResponse>,
    artwork: List<ArtworkResponse>
) {
    var search by remember { mutableStateOf("") }

    // Filtered lists based on search text
    val filteredMuseums = museums.filter { it.fields.name.contains(search, ignoreCase = true) }
    val filteredArtists = artists.filter { it.fields.name.contains(search, ignoreCase = true) }
    val filteredArtworks = artwork.filter { it.fields.name.contains(search, ignoreCase = true) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Row
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
                    text = "SEARCH",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onUserClick) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            // Search Bar
            TextField(
                value = search,
                onValueChange = { newText -> search = newText },
                label = { Text("Find a piece of art, an artist or a museum", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Scrolling Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Museums Section
                if (filteredMuseums.isNotEmpty()) {
                    item {
                        Text(
                            text = "Museums",
                            color = Color.Black,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(filteredMuseums) { museum ->
                        museumItemSearch(museum = museum, onClick = { onMuseumClick(museum.pk) })
                    }
                }

                // Artists Section
                if (filteredArtists.isNotEmpty()) {
                    item {
                        Text(
                            text = "Artists",
                            color = Color.Black,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(filteredArtists) { artist ->
                        artistItemSearch(artist = artist, onClick = { onArtistClick(artist.pk)})
                    }
                }

                // Artwork Section
                if (filteredArtworks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Artworks",
                            color = Color.Black,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(filteredArtworks) { art ->
                        artworkItemSearch(artwork = art, onClick = {onArtworkClick(art.pk)})
                    }
                }
            }
        }
    }
}


@Composable
fun museumItemSearch(museum: MuseumResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp) // Spacing between items
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = museum.fields.image),
            contentDescription = museum.fields.name,
            modifier = Modifier
                .size(120.dp) // Ensures the image is a square
                .clip(RoundedCornerShape(8.dp)), // Optional: Add rounded corners
            contentScale = ContentScale.Crop
        )

        Text(
            text = museum.fields.name,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun artistItemSearch(artist: ArtistResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp) // Spacing between items
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = artist.fields.image),
            contentDescription = artist.fields.name,
            modifier = Modifier
                .size(120.dp) // Ensures the image is a square
                .clip(RoundedCornerShape(8.dp)), // Optional: Add rounded corners
            contentScale = ContentScale.Crop
        )

        Text(
            text = artist.fields.name,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun artworkItemSearch(artwork: ArtworkResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp) // Spacing between items
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = artwork.fields.image),
            contentDescription = artwork.fields.name,
            modifier = Modifier
                .size(120.dp) // Ensures the image is a square
                .clip(RoundedCornerShape(8.dp)), // Optional: Add rounded corners
            contentScale = ContentScale.Crop
        )

        Text(
            text = artwork.fields.name,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically)
                .weight(1f),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

