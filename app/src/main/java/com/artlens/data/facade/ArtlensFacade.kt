package com.artlens.data.facade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse

interface ArtlensFacade {
    fun getArtistDetail(artistId: Int): LiveData<ArtistResponse>
    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse>
}