package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtistResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistListViewModel(private val facade: ArtlensFacade) : ViewModel() {

    // LiveData para observar la lista de artistas
    val artistsLiveData: LiveData<List<ArtistResponse>> = facade.getAllArtists()

    fun fetchAllArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            facade.getAllArtists()
        }
    }
}
