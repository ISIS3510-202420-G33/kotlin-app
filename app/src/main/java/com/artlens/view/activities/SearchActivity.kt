package com.artlens.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.composables.SearchScreen
import com.artlens.view.viewmodels.ArtistListViewModel
import com.artlens.view.viewmodels.ArtistViewModel
import com.artlens.view.viewmodels.ArtworkListViewModel
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.view.viewmodels.ViewModelFactory

class SearchActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private val artistViewModel: ArtistListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private val artworkViewModel: ArtworkListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        museumsViewModel.fetchMuseums()
        artistViewModel.fetchAllArtists()
        artworkViewModel.fetchAllArtworks()

        super.onCreate(savedInstanceState)

        setContent {

            val museums = museumsViewModel.museumsLiveData.observeAsState(emptyList()).value
            val artists = artistViewModel.artistsLiveData.observeAsState(emptyList()).value
            val artwork = artworkViewModel.artworksLiveData.observeAsState(emptyList()).value

            SearchScreen(
                onBackClick = { onBackPressed()},
                onMuseumClick = {},
                onUserClick = {},
                museums = museums,
                artists = artists,
                artwork = artwork

            )

        }

    }
}