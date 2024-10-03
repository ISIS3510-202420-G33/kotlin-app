package com.artlens.data.api

import com.artlens.data.models.ArtworkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalyticsApi {

    // Método para obtener la obra más likeada del mes (espera un array)
    @GET("/analytic_engine/mostliked/")
    fun getArtworkMostLikedMonth(): Call<List<ArtworkResponse>>

    // Método para obtener las recomendaciones de obras basadas en el usuario
    @GET("/analytic_engine/recommend/{id}")
    fun getArtworkRecommendation(@Path("id") userId: Int): Call<List<ArtworkResponse>>
}

