package com.artlens.view.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.services.NetworkReceiver
import com.artlens.data.services.NetworkUtils
import com.artlens.view.composables.NoInternetScreenV2
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

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)


    override fun onCreate(savedInstanceState: Bundle?) {

        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        museumsViewModel.fetchMuseums()
        artistViewModel.fetchAllArtists()
        artworkViewModel.fetchAllArtworks()

        super.onCreate(savedInstanceState)

        setContent {

            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@SearchActivity)
            }
            if (isConnected) {

                val museums = museumsViewModel.museumsLiveData.observeAsState(emptyList()).value
                val artists = artistViewModel.artistsLiveData.observeAsState(emptyList()).value
                val artwork = artworkViewModel.artworksLiveData.observeAsState(emptyList()).value

                SearchScreen(
                    onBackClick = { onBackPressed() },
                    onMuseumClick = { museumId ->
                        val intent = Intent(this, MuseumsDetailActivity::class.java)
                        intent.putExtra("MUSEUM_ID", museumId)
                        startActivity(intent)
                    },
                    onArtistClick = { artistId ->
                        val intent = Intent(this, ArtistDetailActivity::class.java)
                        intent.putExtra("ARTIST_ID", artistId)
                        startActivity(intent)
                    },
                    onArtworkClick = { artworkId ->
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
            else{
                NoInternetScreenV2(
                    onBackClick = { onBackPressed() },
                    textTop = "SEARCH"
                )
            }

        }

    }
}