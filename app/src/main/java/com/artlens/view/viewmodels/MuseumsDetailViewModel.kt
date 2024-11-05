package com.artlens.view.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.MuseumResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MuseumsDetailViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _museumDetail = MutableLiveData<MuseumResponse?>()
    val museumDetail: LiveData<MuseumResponse?> = _museumDetail

    private val _artworksByMuseum = MutableLiveData<List<ArtworkResponse>?>()
    val artworksByMuseum: LiveData<List<ArtworkResponse>?> = _artworksByMuseum

    fun fetchMuseumDetail(museumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = facade.getMuseumDetail(museumId)
                withContext(Dispatchers.Main) {
                    _museumDetail.value = result
                }
            } catch (e: Exception) {
                Log.e("MuseumsDetailViewModel", "Error fetching museum details", e)
            }
        }
    }

    fun fetchArtworksByMuseum(museumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = facade.getArtworksByMuseum(museumId)
                withContext(Dispatchers.Main) {
                    _artworksByMuseum.value = result
                }
            } catch (e: Exception) {
                Log.e("MuseumsDetailViewModel", "Error fetching artworks by museum", e)
            }
        }
    }
}

