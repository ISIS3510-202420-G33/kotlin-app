package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.MuseumResponse

class MuseumsDetailViewModel(private val facade: ArtlensFacade) : ViewModel() {

    fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse> {
        return facade.getMuseumDetail(museumId)
    }

    fun getArtworksByMuseum(museumId: Int): LiveData<List<ArtworkResponse>> {
        return facade.getArtworksByMuseum(museumId)
    }
}
