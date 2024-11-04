package com.artlens.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artlens.data.facade.ArtlensFacade
import com.artlens.viewmodels.RecommendationsViewModel

class ViewModelFactory(private val facade: ArtlensFacade) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ArtistViewModel::class.java) -> {
                ArtistViewModel(facade) as T
            }
            modelClass.isAssignableFrom(ArtworkViewModel::class.java) -> {
                ArtworkViewModel(facade) as T
            }
            modelClass.isAssignableFrom(ArtworkListViewModel::class.java) -> {
                ArtworkListViewModel(facade) as T
            }
            modelClass.isAssignableFrom(MuseumsListViewModel::class.java) -> {
                MuseumsListViewModel(facade) as T
            }
            modelClass.isAssignableFrom(MuseumsDetailViewModel::class.java) -> {
                MuseumsDetailViewModel(facade) as T
            }
            modelClass.isAssignableFrom(CreateAccountViewModel::class.java) -> {
                CreateAccountViewModel(facade) as T
            }
            modelClass.isAssignableFrom(LogInViewModel::class.java) -> {
                LogInViewModel(facade) as T
            }
            modelClass.isAssignableFrom(LikesViewModel::class.java) -> {
                LikesViewModel(facade) as T
            }
            modelClass.isAssignableFrom(ArtistListViewModel::class.java) -> {
                ArtistListViewModel(facade) as T
            }
            modelClass.isAssignableFrom(RecommendationsViewModel::class.java) -> {
                RecommendationsViewModel(facade) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

