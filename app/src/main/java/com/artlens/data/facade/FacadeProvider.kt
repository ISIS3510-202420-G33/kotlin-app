package com.artlens.data.facade

import com.artlens.data.api.ArtistApi
import com.artlens.data.api.ArtworkApi
import com.artlens.data.api.MuseumApi
import com.artlens.data.api.UserApi
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService
import com.artlens.data.services.MuseumService
import com.artlens.data.services.UserService
import com.artlens.network.RetrofitClient

object FacadeProvider {
    private val artistApi = RetrofitClient.createService(ArtistApi::class.java)
    private val artworkApi = RetrofitClient.createService(ArtworkApi::class.java)
    private val museumApi = RetrofitClient.createService(MuseumApi::class.java)
    private val userApi = RetrofitClient.createService(UserApi::class.java)

    private val artistService = ArtistService(artistApi)
    private val artworkService = ArtworkService(artworkApi)
    private val museumService = MuseumService(museumApi)
    private val userService = UserService(userApi)

    val facade: ArtlensFacade by lazy {
        Facade(artistService, artworkService, museumService, userService)
    }
}
