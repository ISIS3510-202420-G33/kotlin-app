
package com.artlens.view.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtworkViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _artworkId = MutableLiveData<Int>()
    val artworkLiveData: LiveData<ArtworkResponse> = _artworkId.switchMap { id ->
        facade.getArtworkDetail(id)
    }

    // LiveData para manejar el estado de "me gusta"
    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> get() = _isLiked

    // LiveData para almacenar el nombre del artista
    private val _artistName = MutableLiveData<String>()
    val artistName: LiveData<String> get() = _artistName

    // Método para obtener los detalles del artista por su ID
    fun fetchArtistName(artistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val artistResponse = facade.getArtistDetail(artistId)

                withContext(Dispatchers.Main) {
                    artistResponse?.let {
                        _artistName.value = it.fields.name
                    }
                }
            } catch (e: Exception) {
                Log.e("ArtistViewModel", "Error fetching artist name", e)
            }
        }
    }

    // Método para verificar si la obra está en los favoritos del usuario
    fun checkIfLiked(userId: Int, artworkId: Int) {
        viewModelScope.launch {
            facade.getLikesByUser(userId).observeForever { likedArtworks ->
                _isLiked.value = likedArtworks.any { it.pk == artworkId }
            }
        }
    }

    // Método para agregar la obra a favoritos
    fun addLike(userId: Int, artworkId: Int) {
        viewModelScope.launch {
            facade.postLikeByUser(userId, artworkId).observeForever { success ->
                if (success) {
                    _isLiked.value = true
                }
            }
        }
    }

    // Método para eliminar la obra de favoritos
    fun removeLike(userId: Int, artworkId: Int) {
        viewModelScope.launch {
            facade.deleteLikeByUser(userId, artworkId).observeForever { success ->
                if (success) {
                    _isLiked.value = false
                }
            }
        }
    }

    // Método para alternar el estado de "me gusta"
    fun toggleLike(userId: Int, artworkId: Int) {
        if (_isLiked.value == true) {
            removeLike(userId, artworkId)
        } else {
            addLike(userId, artworkId)
        }
    }

    fun fetchArtworkDetail(artworkId: Int, userId: Int) {
        _artworkId.value = artworkId
        checkIfLiked(userId, artworkId)
    }

    fun downloadFavorites(likedMuseums: List<ArtworkResponse>, context: Context) {

        facade.downloadFavorites(likedMuseums, context)


    }
}

