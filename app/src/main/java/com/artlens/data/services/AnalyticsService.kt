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
    suspend fun getArtworkMostLikedMonth(): ArtworkResponse? {
        val response = analyticsApi.getArtworkMostLikedMonth() // Llamada a la API de Retrofit

        return if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
            val firstArtwork = response.body()?.first()
            Log.d("AnalyticsService", "MostLikedArtwork received: $firstArtwork")
            firstArtwork // Devuelve un `ArtworkResponse?`
        } else {
            Log.e("AnalyticsService", "Error in response: ${response.errorBody()?.string()}")
            null
        }
    }


    // Método para obtener las recomendaciones de obras basadas en el usuario
    suspend fun getArtworkRecommendation(userId: Int): List<ArtworkResponse> {
        return try {
            val response = analyticsApi.getArtworkRecommendation(userId)

            if (response.isSuccessful && response.body() != null) {
                Log.d("AnalyticsService", "Recommendations received: ${response.body()}")
                response.body() ?: emptyList() // Devuelve la lista de recomendaciones
            } else {
                Log.e("AnalyticsService", "Error in response: ${response.errorBody()?.string()}")
                emptyList() // Devuelve una lista vacía en caso de error
            }
        } catch (e: Exception) {
            Log.e("AnalyticsService", "Network failure: ${e.message}")
            emptyList() // Devuelve una lista vacía en caso de fallo de red
        }
    }

}



