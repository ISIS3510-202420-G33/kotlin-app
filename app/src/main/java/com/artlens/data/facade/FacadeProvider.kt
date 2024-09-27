package com.artlens.data.facade

import com.artlens.data.api.ArtistApi
import com.artlens.data.api.ArtworkApi
import com.artlens.data.services.ArtistService
import com.artlens.data.services.ArtworkService
import com.artlens.network.RetrofitClient

object FacadeProvider {
    private val artistApi = RetrofitClient.createService(ArtistApi::class.java)
    private val artworkApi = RetrofitClient.createService(ArtworkApi::class.java)

    private val artistService = ArtistService(artistApi)
    private val artworkService = ArtworkService(artworkApi)

    val facade: ArtlensFacade by lazy {
        Facade(artistService, artworkService)
    }
}
