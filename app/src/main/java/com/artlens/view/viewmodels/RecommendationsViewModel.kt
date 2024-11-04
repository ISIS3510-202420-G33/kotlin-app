package com.artlens.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecommendationsViewModel(private val analyticsService: ArtlensFacade) : ViewModel() {

    private val _recommendations = MutableLiveData<List<ArtworkResponse>>()
    val recommendations: MutableLiveData<List<ArtworkResponse>> = _recommendations

    private val _mostLikedArtwork = MutableLiveData<ArtworkResponse?>()
    val mostLikedArtwork: MutableLiveData<ArtworkResponse?> = _mostLikedArtwork

    fun fetchMostLikedArtwork() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val artwork = analyticsService.getArtworkMostLikedMonth()

                withContext(Dispatchers.Main) {
                    _mostLikedArtwork.value = artwork
                    Log.d("RecommendationsViewModel", "MostLikedArtwork updated: $artwork")
                }
            } catch (e: Exception) {
                Log.e("RecommendationsViewModel", "Error fetching most liked artwork", e)
            }
        }
    }

    // Método para obtener las recomendaciones
    fun fetchArtworkRecommendations(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val artworks = analyticsService.getArtworkRecommendation(userId)

                withContext(Dispatchers.Main) {
                    _recommendations.value = artworks
                }
            } catch (e: Exception) {
                Log.e("RecommendationsViewModel", "Error fetching recommendations", e)
            }
        }
    }
}

