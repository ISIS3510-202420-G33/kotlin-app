package com.artlens.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse

class RecommendationsViewModel(private val analyticsService: ArtlensFacade) : ViewModel() {

    private val _mostLikedArtwork = MutableLiveData<ArtworkResponse>()
    val mostLikedArtwork: LiveData<ArtworkResponse> = _mostLikedArtwork

    private val _recommendations = MutableLiveData<List<ArtworkResponse>>()
    val recommendations: LiveData<List<ArtworkResponse>> = _recommendations

    // Método para obtener la obra más likeada
    fun fetchMostLikedArtwork() {
        analyticsService.getArtworkMostLikedMonth().observeForever { artwork ->
            _mostLikedArtwork.value = artwork
            Log.d("RecommendationsViewModel", "MostLikedArtwork updated: $artwork")
        }
    }

    // Método para obtener las recomendaciones
    fun fetchArtworkRecommendations(userId: Int) {
        analyticsService.getArtworkRecommendation(userId).observeForever { artworks ->
            _recommendations.value = artworks
            Log.d("RecommendationsViewModel", "Recommendations updated: $artworks")
        }
    }
}

