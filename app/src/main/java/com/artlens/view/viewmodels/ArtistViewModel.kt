package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtistResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _artistLiveData = MutableLiveData<ArtistResponse>()
    val artistLiveData: LiveData<ArtistResponse> = _artistLiveData

    fun fetchArtistDetail(artistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val artistResponse = facade.getArtistDetail(artistId)
            withContext(Dispatchers.Main) {
                artistResponse.observeForever { artist ->
                    _artistLiveData.value = artist
                }
            }
        }
    }
}
