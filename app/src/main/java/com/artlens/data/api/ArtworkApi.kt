package com.artlens.data.api

import com.artlens.data.models.ArtistResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.artlens.data.models.ArtworkResponse

interface ArtworkApi {
    @GET("artworks/{id}")
    fun getArtworkDetail(@Path("id") id: Int): Call<List<ArtworkResponse>>

    @GET("artworks/")
    fun getAllArtworks(): Call<List<ArtworkResponse>>

    @GET("artworks/museum/{id}")
    fun getArtworksByMuseum(@Path("id") id: Int): Call<List<ArtworkResponse>>

    @GET("artworks/artist/{id}")
    fun getArtworksByArtist(@Path("id") id: Int): Call<List<ArtworkResponse>>
}


