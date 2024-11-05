package com.artlens.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.artlens.data.models.MuseumResponse
import retrofit2.Response

interface MuseumApi {

    @GET("museums/{id}")
    suspend fun getMuseumDetail(@Path("id") id: Int): Response<List<MuseumResponse>>

    @GET("museums/")
    suspend fun getAllMuseums(): Response<List<MuseumResponse>>
}
