package com.artlens.view.viewmodels

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.ArtworkResponse
import com.artlens.utils.UserPreferences
import java.io.OutputStream

class LikesViewModel(private val facade: ArtlensFacade) : ViewModel() {
    private val _likedMuseums = MutableLiveData<List<ArtworkResponse>>()
    val likedMuseums: LiveData<List<ArtworkResponse>> = _likedMuseums

    fun fetchLikedMuseums(userId: Int) {
        facade.getLikesByUser(userId).observeForever { likedArtworks ->
            val museums = likedArtworks.filter { it.fields.museum > 0 }
            _likedMuseums.value = museums
        }
    }

    fun removeLike(artworkId: Int) {
        val user = UserPreferences.getPk()

        if (user != null) {
            facade.deleteLikeByUser(userId = user, artworkId).observeForever { success ->
                if (success) {
                    fetchLikedMuseums(userId = user)
                }
            }
        }
    }

}
