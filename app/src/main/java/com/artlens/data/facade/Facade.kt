package com.artlens.data.facade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.data.models.UserResponse
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService
import com.artlens.data.services.MuseumService
import com.artlens.data.services.UserService

class Facade(
    private val artistService: ArtistService,
    private val artworkService: ArtworkService,
    private val museumService: MuseumService,
    private val userService: UserService
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

    // Agregar la función para obtener todas las obras de arte
    override fun getAllArtworks(): LiveData<List<ArtworkResponse>> {
        return artworkService.getAllArtworks()
    }

    override fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse> {
        return museumService.getMuseumDetail(museumId)
    }

    override fun getAllMuseums(): LiveData<List<MuseumResponse>> {
        return museumService.getAllMuseums()
    }

    override fun createUser(email: String, userName: String, name: String, password: String): MutableLiveData<CreateUserResponse> {
        return userService.createUser(email, userName, name, password)
    }
}
