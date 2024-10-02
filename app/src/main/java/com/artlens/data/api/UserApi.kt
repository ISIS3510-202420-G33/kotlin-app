package com.artlens.data.api

import com.artlens.data.models.UserAuth
import com.artlens.data.models.UserFields
import com.artlens.data.models.UserResponse
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.LikeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("/user/create")
    fun createUser(@Body user: UserFields) : Call<List<UserResponse>>

    @POST("/user/authenticate")
    fun authenticateUser(@Body user: UserAuth) : Call<List<UserResponse>>

    @POST("/user/like")
    fun postLikeByUser(@Body likeRequest: LikeRequest): Call<Void>

    @GET("/user/liked/{userId}")
    fun getLikesByUser(@Path("userId") userId: Int): Call<List<ArtworkResponse>>

    @DELETE("/user/liked/{userId}/{artworkId}")
    fun deleteLikeByUser(@Path("userId") userId: Int, @Path("artworkId") artworkId: Int): Call<Void>
}
