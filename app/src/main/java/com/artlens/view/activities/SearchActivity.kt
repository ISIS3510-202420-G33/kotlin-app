package com.artlens.view.activities

import android.content.Intent
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
                onMuseumClick = {museumId ->
                    val intent = Intent(this, MuseumsDetailActivity::class.java)
                    intent.putExtra("MUSEUM_ID", museumId)
                    startActivity(intent)
                },
                onArtistClick = {artistId ->
                    val intent = Intent(this, ArtistDetailActivity::class.java)
                    intent.putExtra("ARTIST_ID", artistId)
                    startActivity(intent)
                },
                onArtworkClick = {artworkId ->
                    val intent = Intent(this, ArtworkDetailActivity::class.java)
                    intent.putExtra("id", artworkId)
                    startActivity(intent)
                },
                onUserClick = {},
                museums = museums,
                artists = artists,
                artwork = artwork

            )

        }

    }
}