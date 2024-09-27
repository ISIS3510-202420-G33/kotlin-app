package com.artlens.data.facade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artlens.view.viewmodels.ArtistViewModel
import com.artlens.view.viewmodels.ArtworkViewModel

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
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
