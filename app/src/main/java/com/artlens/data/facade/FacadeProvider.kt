package com.artlens.data.facade

import com.artlens.data.api.AnalyticsApi
import com.artlens.data.api.ArtistApi
import com.artlens.data.api.ArtworkApi
import com.artlens.data.api.CommentApi
import com.artlens.data.api.MuseumApi
import com.artlens.data.api.UserApi
import com.artlens.data.cache.ArtistCache
import com.artlens.data.cache.ArtworkCache
import com.artlens.data.cache.MuseumCache
import com.artlens.data.services.AnalyticsService
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService
import com.artlens.data.services.CommentService
import com.artlens.data.services.MuseumService
import com.artlens.data.services.UserService
import com.artlens.network.RetrofitClient

object FacadeProvider {
    private val artistApi = RetrofitClient.createService(ArtistApi::class.java)
    private val artworkApi = RetrofitClient.createService(ArtworkApi::class.java)
    private val museumApi = RetrofitClient.createService(MuseumApi::class.java)
    private val userApi = RetrofitClient.createService(UserApi::class.java)
    private val commentApi = RetrofitClient.createService(CommentApi::class.java)
    private val analyticsApi = RetrofitClient.createService(AnalyticsApi::class.java)
    private val artworkCache = ArtworkCache()
    private val museumCache = MuseumCache()
    private val artistCache = ArtistCache()
    private val artistService = ArtistService(artistApi, artistCache)
    private val artworkService = ArtworkService(artworkApi, artworkCache)
    private val museumService = MuseumService(museumApi, museumCache)
    private val userService = UserService(userApi)
    private val commentService = CommentService(commentApi)
    private val analyticsService = AnalyticsService(analyticsApi)

    val facade: ArtlensFacade by lazy {
        Facade(artistService, artworkService, museumService, userService, commentService, analyticsService)
    }
}
