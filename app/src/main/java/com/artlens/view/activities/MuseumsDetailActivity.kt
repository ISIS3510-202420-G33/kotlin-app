package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.view.viewmodels.MuseumsDetailViewModel
import com.artlens.view.composables.MuseumDetailScreen

class MuseumsDetailActivity : ComponentActivity() {

    private val museumsDetailViewModel: MuseumsDetailViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val museumId = intent.getIntExtra("MUSEUM_ID", 1)

        setContent {
            val museumState by museumsDetailViewModel.museumDetail.observeAsState()
            val artworksState by museumsDetailViewModel.artworksByMuseum.observeAsState(emptyList())

            LaunchedEffect(Unit) {
                museumsDetailViewModel.fetchMuseumDetail(museumId)
                museumsDetailViewModel.fetchArtworksByMuseum(museumId)
            }

            artworksState?.map { it.fields.image }?.let {
                MuseumDetailScreen(
                    museum = museumState,
                    artworkUrls = it.take(5),
                    onBackClick = { onBackPressed() },
                    onHomeClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
