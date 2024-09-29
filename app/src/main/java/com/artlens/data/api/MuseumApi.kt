package com.artlens.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.artlens.data.models.MuseumResponse

interface MuseumApi {
    @GET("museums/{id}")
    fun getMuseumDetail(@Path("id") id: Int): Call<List<MuseumResponse>>

    @GET("museums/")
    fun getAllMuseums(): Call<List<MuseumResponse>>
}