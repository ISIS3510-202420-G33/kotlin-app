package com.artlens.data.facade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.models.ArtistResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.CommentResponse
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.MuseumResponse
import com.artlens.data.models.UserResponse
import com.artlens.data.services.AnalyticsService
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService
import com.artlens.data.services.CommentService
import com.artlens.data.services.MuseumService
import com.artlens.data.services.UserService

class Facade(
    private val artistService: ArtistService,
    private val artworkService: ArtworkService,
    private val museumService: MuseumService,
    private val userService: UserService,
    private val commentService: CommentService,
    private val analyticsService: AnalyticsService
) : ArtlensFacade {

    //ArtistService
    override suspend fun getArtistDetail(artistId: Int): ArtistResponse? {
        return artistService.getArtistDetail(artistId)
    }

    override fun getAllArtists(): LiveData<List<ArtistResponse>> {
        return artistService.getAllArtists()
    }

    //ArtworkService
    override fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse> {
        return artworkService.getArtworkDetail(artworkId)
    }

    override fun getAllArtworks(): LiveData<List<ArtworkResponse>> {
        return artworkService.getAllArtworks()
    }

    override fun getArtworksByArtist(artistId: Int): LiveData<List<ArtworkResponse>> {
        return artworkService.getArtworksByArtist(artistId)
    }

    override fun getArtworksByMuseum(museumId: Int): LiveData<List<ArtworkResponse>> {
        return artworkService.getArtworksByMuseum(museumId)
    }

    //MuseumService
    override fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse> {
        return museumService.getMuseumDetail(museumId)
    }

    override fun getAllMuseums(): LiveData<List<MuseumResponse>> {
        return museumService.getAllMuseums()
    }


    //UserService
    override fun createUser(email: String, userName: String, name: String, password: String): MutableLiveData<CreateUserResponse> {
        return userService.createUser(email, userName, name, password)
    }

    override fun authenticateUser(userName: String, password: String): MutableLiveData<CreateUserResponse> {
        return userService.authenticateUser(userName, password)
    }

    // LikesService (UserService)
    override fun postLikeByUser(userId: Int, artworkId: Int): LiveData<Boolean> {
        return userService.postLikeByUser(userId, artworkId)
    }

    override fun getLikesByUser(userId: Int): LiveData<List<ArtworkResponse>> {
        return userService.getLikesByUser(userId)
    }

    override fun deleteLikeByUser(userId: Int, artworkId: Int): LiveData<Boolean> {
        return userService.deleteLikeByUser(userId, artworkId)
    }

    //Comments
    override fun postComment(content: String, date: String, artworkId: Int, userId: Int): LiveData<Boolean> {
        return commentService.postComment(content, date, artworkId, userId)
    }

    override fun getCommentsByArtwork(artworkId: Int): LiveData<List<CommentResponse>> {
        return commentService.getCommentsByArtwork(artworkId)
    }

    //Analytics
    override suspend fun getArtworkMostLikedMonth(): ArtworkResponse? {
        return analyticsService.getArtworkMostLikedMonth()
    }

    override suspend fun getArtworkRecommendation(userId: Int): List<ArtworkResponse> {
        return analyticsService.getArtworkRecommendation(userId)
    }
}
