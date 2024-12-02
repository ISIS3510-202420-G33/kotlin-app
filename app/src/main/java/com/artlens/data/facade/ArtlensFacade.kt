package com.artlens.data.facade

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.CommentResponse
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.data.models.UserResponse
import retrofit2.Call

interface ArtlensFacade {
    //ArtistAPI
    suspend fun getArtistDetail(artistId: Int): ArtistResponse?
    fun getAllArtists(): LiveData<List<ArtistResponse>>

    //ArtworkAPI
    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse>
    fun getAllArtworks(): LiveData<List<ArtworkResponse>>
    fun getArtworksByArtist(artistId: Int): LiveData<List<ArtworkResponse>>
    suspend fun getArtworksByMuseum(museumId: Int): List<ArtworkResponse>
    fun downloadFavorites(likedMuseums: List<ArtworkResponse>, context: Context)

    //MuseumAPI
    suspend fun getMuseumDetail(museumId: Int): MuseumResponse?
    suspend fun getAllMuseums(): List<MuseumResponse>?

    //UserAPI
    fun createUser(email: String, userName: String, name: String, password: String): LiveData<CreateUserResponse>
    fun authenticateUser(userName: String, password: String): LiveData<CreateUserResponse>

    // Likes API
    fun postLikeByUser(userId: Int, artworkId: Int): LiveData<Boolean>
    fun getLikesByUser(userId: Int): LiveData<List<ArtworkResponse>>
    fun deleteLikeByUser(userId: Int, artworkId: Int): LiveData<Boolean>

    //Comments
    fun postComment(content: String, date: String, artworkId: Int, userId: Int): LiveData<Boolean>
    fun getCommentsByArtwork(artworkId: Int): LiveData<List<CommentResponse>>

    //Analytics
    suspend fun getArtworkMostLikedMonth(): ArtworkResponse?
    suspend fun getArtworkRecommendation(userId: Int): List<ArtworkResponse>
}