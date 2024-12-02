package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.CommentApi
import com.artlens.data.models.CommentRequest
import com.artlens.data.models.CommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentService(private val commentApi: CommentApi) {

    fun postComment(content: String, date: String, artworkId: Int, userId: Int): LiveData<Boolean> {
        val commentLiveData = MutableLiveData<Boolean>()
        val commentRequest = CommentRequest(content, date, artworkId, userId)

        commentApi.postComment(commentRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CommentService", "Comment posted successfully")
                    commentLiveData.postValue(true)
                } else {
                    Log.e("CommentService", "Error posting comment: ${response.code()}")
                    commentLiveData.postValue(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CommentService", "Failure posting comment: ${t.message}")
                commentLiveData.postValue(false)
            }
        })

        return commentLiveData
    }

    fun getCommentsByArtwork(artworkId: Int): LiveData<List<CommentResponse>> {
        val commentsLiveData = MutableLiveData<List<CommentResponse>>()

        commentApi.getCommentsByArtwork(artworkId).enqueue(object : Callback<List<CommentResponse>> {
            override fun onResponse(
                call: Call<List<CommentResponse>>,
                response: Response<List<CommentResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { comments ->
                        Log.d("CommentService", "Fetched ${comments.size} comments")
                        commentsLiveData.postValue(comments)
                    } ?: run {
                        Log.e("CommentService", "Empty response body")
                        commentsLiveData.postValue(emptyList())
                    }
                } else {
                    Log.e("CommentService", "Error fetching comments: ${response.code()}")
                    commentsLiveData.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                Log.e("CommentService", "Failure fetching comments: ${t.message}")
                commentsLiveData.postValue(emptyList())
            }
        })

        return commentsLiveData
    }
}

