package com.artlens.data.facade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.data.models.UserResponse

interface ArtlensFacade {
    fun getArtistDetail(artistId: Int): LiveData<ArtistResponse>
    fun getAllArtists(): LiveData<List<ArtistResponse>>
    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse>
    fun getAllArtworks(): LiveData<List<ArtworkResponse>>
    fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse>
    fun getAllMuseums(): LiveData<List<MuseumResponse>>
    fun createUser(email: String, userName: String, name: String, password: String): LiveData<CreateUserResponse>
    fun authenticateUser(userName: String, password: String): LiveData<CreateUserResponse>
}