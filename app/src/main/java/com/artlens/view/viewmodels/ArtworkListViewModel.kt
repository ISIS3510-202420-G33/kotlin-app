package com.artlens.view.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtworkListViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _artworksLiveData = MutableLiveData<List<ArtworkResponse>>()
    val artworksLiveData: LiveData<List<ArtworkResponse>> = _artworksLiveData

    // Función para obtener todas las obras de arte
    fun fetchAllArtworks() {
        viewModelScope.launch(Dispatchers.IO) {
            val artworkResponse = facade.getAllArtworks()
            withContext(Dispatchers.Main) {
                artworkResponse.observeForever { artworks ->
                    Log.d("ArtworkListViewModel", "Artworks received: ${artworks.size}")
                    _artworksLiveData.value = artworks
                }
            }
        }
    }
}
