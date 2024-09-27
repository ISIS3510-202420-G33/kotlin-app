package com.artlens.data.facade

import androidx.lifecycle.LiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService

class Facade(
    private val artistService: ArtistService,
    private val artworkService: ArtworkService
) : ArtlensFacade {

    override fun getArtistDetail(artistId: Int): LiveData<ArtistResponse> {
        return artistService.getArtistDetail(artistId)
    }

    override fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse> {
        return artworkService.getArtworkDetail(artworkId)
    }
}
