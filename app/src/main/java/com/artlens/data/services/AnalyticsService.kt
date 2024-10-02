package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.AnalyticsApi
import com.artlens.data.models.ArtworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyticsService(private val analyticsApi: AnalyticsApi) {

    // Obtener la obra más likeada del mes (una sola obra)
    fun getArtworkMostLikedMonth(): LiveData<ArtworkResponse> {
        val artworkLiveData = MutableLiveData<ArtworkResponse>()

        analyticsApi.getArtworkMostLikedMonth().enqueue(object : Callback<ArtworkResponse> {
            override fun onResponse(call: Call<ArtworkResponse>, response: Response<ArtworkResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    // Asignamos la obra más likeada al LiveData
                    artworkLiveData.value = response.body()
                } else {
                    Log.e("AnalyticsService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ArtworkResponse>, t: Throwable) {
                artworkLiveData.value = null // Manejo de error, devolvemos nulo
                Log.e("AnalyticsService", "Failure: ${t.message}")
            }
        })

        return artworkLiveData
    }

    // Obtener las recomendaciones de obras de arte para un usuario específico
    fun getArtworkRecommendation(userId: Int): LiveData<List<ArtworkResponse>> {
        val recommendationsLiveData = MutableLiveData<List<ArtworkResponse>>()

        analyticsApi.getArtworkRecommendation(userId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    recommendationsLiveData.value = response.body()
                } else {
                    Log.e("AnalyticsService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                recommendationsLiveData.value = emptyList()  // Retornamos una lista vacía si falla
                Log.e("AnalyticsService", "Failure: ${t.message}")
            }
        })

        return recommendationsLiveData
    }
}
