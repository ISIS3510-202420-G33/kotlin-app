package com.artlens.data.facade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.data.models.UserResponse

interface ArtlensFacade {
    //ArtistAPI
    fun getArtistDetail(artistId: Int): LiveData<ArtistResponse>
    fun getAllArtists(): LiveData<List<ArtistResponse>>

    //ArtworkAPI
    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse>
    fun getAllArtworks(): LiveData<List<ArtworkResponse>>
    fun getArtworksByArtist(artistId: Int): LiveData<List<ArtworkResponse>>
    fun getArtworksByMuseum(museumId: Int): LiveData<List<ArtworkResponse>>

    //MuseumAPI
    fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse>
    fun getAllMuseums(): LiveData<List<MuseumResponse>>

    //UserAPI
    fun createUser(email: String, userName: String, name: String, password: String): LiveData<CreateUserResponse>
    fun authenticateUser(userName: String, password: String): LiveData<CreateUserResponse>

}