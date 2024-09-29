package com.artlens.data.facade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.MuseumResponse

interface ArtlensFacade {
    fun getArtistDetail(artistId: Int): LiveData<ArtistResponse>
    fun getAllArtists(): LiveData<List<ArtistResponse>>
    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse>
    fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse>
    fun getAllMuseums(): LiveData<List<MuseumResponse>>
}