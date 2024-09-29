package com.artlens.data.facade

import androidx.lifecycle.LiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService
import com.artlens.data.services.MuseumService

class Facade(
    private val artistService: ArtistService,
    private val artworkService: ArtworkService,
    private val museumService: MuseumService
) : ArtlensFacade {

    override fun getArtistDetail(artistId: Int): LiveData<ArtistResponse> {
        return artistService.getArtistDetail(artistId)
    }

    override fun getAllArtists(): LiveData<List<ArtistResponse>> {
        return artistService.getAllArtists()
    }

    override fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse> {
        return artworkService.getArtworkDetail(artworkId)
    }

    override fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse> {
        return museumService.getMuseumDetail(museumId)
    }

    override fun getAllMuseums(): LiveData<List<MuseumResponse>> {
        return museumService.getAllMuseums()
    }
}
