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

    // Método para obtener la obra más likeada del mes
    fun getArtworkMostLikedMonth(): LiveData<ArtworkResponse> {
        val mostLikedArtworkLiveData = MutableLiveData<ArtworkResponse>()

        analyticsApi.getArtworkMostLikedMonth().enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val firstArtwork = response.body()?.first()
                    Log.d("AnalyticsService", "MostLikedArtwork received: $firstArtwork")
                    mostLikedArtworkLiveData.value = firstArtwork
                } else {
                    Log.e("AnalyticsService", "Error in response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("AnalyticsService", "Failure: ${t.message}")
            }
        })

        return mostLikedArtworkLiveData
    }

    // Método para obtener las recomendaciones de obras basadas en el usuario
    fun getArtworkRecommendation(userId: Int): LiveData<List<ArtworkResponse>> {
        val recommendationsLiveData = MutableLiveData<List<ArtworkResponse>>()

        analyticsApi.getArtworkRecommendation(userId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d("AnalyticsService", "Recommendations received: ${response.body()}")
                    recommendationsLiveData.value = response.body()
                } else {
                    Log.e("AnalyticsService", "Error in response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("AnalyticsService", "Failure: ${t.message}")
            }
        })

        return recommendationsLiveData
    }
}



