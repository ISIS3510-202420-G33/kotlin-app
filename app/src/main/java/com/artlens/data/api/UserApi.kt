package com.artlens.data.api

import retrofit2.Call
import com.artlens.data.models.UserFields
import retrofit2.http.Body
import retrofit2.http.POST
import com.artlens.data.models.UserResponse

interface UserApi {

    @POST("/user/create")
    fun createUser(@Body user: UserFields) : Call<List<UserResponse>>

}