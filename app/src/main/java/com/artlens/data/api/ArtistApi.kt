package com.artlens.data.api

import com.artlens.data.models.ArtistResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistApi {
    @GET("artists/{id}")
    suspend fun getArtistDetail(@Path("id") id: Int): Response<List<ArtistResponse>>

    @GET("artists/")
    fun getAllArtists(): Call<List<ArtistResponse>>
}