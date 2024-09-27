package com.artlens.data.api

import com.artlens.data.models.ArtistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistApi {
    @GET("artist/{id}")
    fun getArtistDetail(@Path("id") id: Int): Call<List<ArtistResponse>>
}