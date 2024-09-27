package com.artlens.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.artlens.data.models.ArtworkResponse

interface ArtworkApi {
    @GET("artwork/{id}")
    fun getArtworkDetail(@Path("id") id: Int): Call<List<ArtworkResponse>>
}


