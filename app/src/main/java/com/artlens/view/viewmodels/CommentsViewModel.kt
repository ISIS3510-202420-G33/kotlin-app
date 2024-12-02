package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.CommentResponse

class CommentsViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _isCommentPosted = MutableLiveData<Boolean>()

    val commentsLiveData: MutableLiveData<List<CommentResponse>> = MutableLiveData()

    fun fetchCommentsByArtwork(artworkId: Int) {
        facade.getCommentsByArtwork(artworkId).observeForever { comments ->
            commentsLiveData.postValue(comments)
        }
    }

    fun postComment(content: String, date: String, artworkId: Int, userId: Int) {
        facade.postComment(content, date, artworkId, userId).observeForever { success ->
            _isCommentPosted.postValue(success)
            if (success) {
                fetchCommentsByArtwork(artworkId)
            }
        }
    }

}
