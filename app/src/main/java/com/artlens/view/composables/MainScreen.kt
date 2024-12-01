package com.artlens.view.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import coil.compose.rememberAsyncImagePainter
import com.artlens.R
import com.artlens.utils.UserPreferences

@Composable
fun MainScreen(
    imageUrls: List<String>,
    museumIds: List<Int>,
    onMapClick: () -> Unit,
    onMuseumClick: (Int) -> Unit,
    onRecommendationClick: () -> Unit,
    onArtistClick: () -> Unit,
    onBackClick: () -> Unit,
    onUserClick: () -> Unit,
    onCameraClick: () -> Unit,
    onMuseumsClick: () -> Unit,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    logOutClick: () -> Unit,
    onViewFavoritesClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hi ${UserPreferences.getUsername()}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Black)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            onDismissDialog()
                            onViewFavoritesClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Text("View Favorites")
                    }

                    Button(
                        onClick = {
                            logOutClick()
                            onDismissDialog()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Text("Log Out")
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
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
                    text = "HOME",
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

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0), shape = CircleShape)
                    .clickable { onSearchClick() }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Find a piece of art, an artist or a museum",
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Museums in your city",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            MuseumImageCarousel(
                imageUrls = imageUrls,
                onMuseumClick = onMuseumClick,
                museumIds = museumIds
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = onMuseumsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("View Museums")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onMapClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("View Map")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onArtistClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("View Artists")
            }
        }

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

            IconButton(onClick = onCameraClick) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(onClick = onRecommendationClick) {
                Image(
                    painter = painterResource(id = R.drawable.fire),
                    contentDescription = "Recommendations",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun MuseumImageCarousel(
    imageUrls: List<String>,
    onMuseumClick: (Int) -> Unit,
    museumIds: List<Int>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        itemsIndexed(imageUrls) { index, imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable { onMuseumClick(museumIds[index]) },
                contentScale = ContentScale.Crop
            )
        }
    }
}

