
package com.artlens.view.viewmodels

import androidx.lifecycle.*
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import kotlinx.coroutines.launch

class ArtworkViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _artworkId = MutableLiveData<Int>()
    val artworkLiveData: LiveData<ArtworkResponse> = _artworkId.switchMap { id ->
        facade.getArtworkDetail(id)
    }

    // LiveData para manejar el estado de "me gusta"
    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> get() = _isLiked

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
                // Aquí puedes manejar errores si lo deseas
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
                // Aquí puedes manejar errores si lo deseas
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

    // Método para establecer el ID de la obra y verificar el estado de "me gusta"
    fun fetchArtworkDetail(artworkId: Int, userId: Int) {
        _artworkId.value = artworkId
        checkIfLiked(userId, artworkId)
    }
}
