package com.artlens.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.composables.LikesScreen
import com.artlens.view.viewmodels.ArtworkListViewModel

class LikesActivity : ComponentActivity() {

    // Initialize ViewModel using FacadeProvider
    private val artworkListViewModel: ArtworkListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val likedArtworks by artworkListViewModel.likedArtworks.observeAsState(emptyList())

            LikesScreen(
                artworkListViewModel = artworkListViewModel,
                onBackClick = { finish() },  // Navigate back
                onArtworkClick = { artworkId ->
                    // Navigate to ArtworkDetailActivity with the artworkId
                }
            )

            // Fetch liked artworks when the screen loads
            artworkListViewModel.fetchLikedArtworks()
        }
    }
}
