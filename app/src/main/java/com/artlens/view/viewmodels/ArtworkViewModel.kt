package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtworkViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _artworkLiveData = MutableLiveData<ArtworkResponse>()
    val artworkLiveData: LiveData<ArtworkResponse> = _artworkLiveData

    fun fetchArtworkDetail(artworkId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val artworkResponse = facade.getArtworkDetail(artworkId)
            withContext(Dispatchers.Main) {
                artworkResponse.observeForever { artwork ->
                    _artworkLiveData.value = artwork
                }
            }
        }
    }
}
