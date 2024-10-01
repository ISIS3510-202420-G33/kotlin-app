package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.UserApi
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.UserAuth
import com.artlens.data.models.UserFields
import com.artlens.data.models.UserResponse
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class UserService(private val userApi: UserApi) {

    fun createUser(email: String, userName: String, name: String, password: String): MutableLiveData<CreateUserResponse> {

        val userLiveData = MutableLiveData<CreateUserResponse>()
        val createUser = UserFields(name, userName, email, password)

        userApi.createUser(createUser).enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { userResponseList ->
                        if (userResponseList.isNotEmpty()) {
                            val createdUser = userResponseList[0] // Get the first user from the list
                            Log.d("UserService", "User Created: $createdUser")
                            userLiveData.value = CreateUserResponse.Success(createdUser) // Success case
                        } else {
                            userLiveData.value = CreateUserResponse.Failure("No user created.")
                        }
                    } ?: run {
                        userLiveData.value = CreateUserResponse.Failure("Response body is null.")
                    }
                } else {
                    val err = response.errorBody()?.string()
                    Log.e("UserService", "Error: $err")
                    userLiveData.value = CreateUserResponse.Failure("Error: $err")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e("UserService", "Failure: ${t.message}")
                userLiveData.value = CreateUserResponse.Failure("Failure: ${t.message}")

            }
        })

        return userLiveData
    }

    fun authenticateUser(userName: String, password: String): MutableLiveData<CreateUserResponse> {

        val userLiveData = MutableLiveData<CreateUserResponse>()
        val userAuth = UserAuth(userName, password)

        userApi.authenticateUser(userAuth).enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {

                if (response.isSuccessful) {
                    response.body()?.let { userResponseList ->
                        if (userResponseList.isNotEmpty()) {
                            val authorizedUser = userResponseList[0] // Get the first user from the list
                            Log.d("UserService", "User Authenticated: $authorizedUser")
                            userLiveData.value = CreateUserResponse.Success(authorizedUser)
                        } else {
                            userLiveData.value = CreateUserResponse.Failure("Authentication error")
                        }
                    }?: run {
                        userLiveData.value = CreateUserResponse.Failure("Response body is null.")
                    }
                } else {
                    val err = response.errorBody()?.string()
                    Log.e("UserService", "Error: $err")
                    userLiveData.value = CreateUserResponse.Failure("Error: $err")
                }

            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.e("UserService", "Failure: ${t.message}")
                userLiveData.value = CreateUserResponse.Failure("Failure: ${t.message}")

            }
        })

        return userLiveData

    }



}
