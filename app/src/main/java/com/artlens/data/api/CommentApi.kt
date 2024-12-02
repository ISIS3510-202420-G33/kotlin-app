
package com.artlens.data.api

import androidx.lifecycle.LiveData
import com.artlens.data.models.CommentRequest
import com.artlens.data.models.CommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {

    @POST("/comments/")
    fun postComment(@Body comment: CommentRequest): Call<Void>

    @GET("artworks/comments/{artworkId}")
    fun getCommentsByArtwork(@Path("artworkId") artworkId: Int): Call<List<CommentResponse>>
}
