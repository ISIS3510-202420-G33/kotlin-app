package com.artlens.data.services

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.UserApi
import com.artlens.data.models.ArtworkResponse
import com.artlens.data.models.CreateUserResponse
import com.artlens.data.models.LikeRequest
import com.artlens.data.models.UserAuth
import com.artlens.data.models.UserFields
import com.artlens.data.models.UserResponse
import com.artlens.utils.UserPreferences
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class UserService(private val userApi: UserApi) {

    fun createUser(email: String, userName: String, name: String, password: String): MutableLiveData<CreateUserResponse> {

        val db = Firebase.firestore

        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "Funcionalidad" to "Fun5",
            "Fecha" to Timestamp.now()
        )

        // Add a new document with a generated ID
        db.collection("BQ33")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

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

        val db = Firebase.firestore

        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "Funcionalidad" to "Fun5",
            "Fecha" to Timestamp.now()
        )

        // Add a new document with a generated ID
        db.collection("BQ33")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

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

                            //save Info
                            UserPreferences.saveUser(pk = authorizedUser.pk, username = authorizedUser.fields.userName, email = authorizedUser.fields.email)


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

    fun postLikeByUser(userId: Int, artworkId: Int): MutableLiveData<Boolean> {
        val likeLiveData = MutableLiveData<Boolean>()
        val likeRequest = LikeRequest(userId, artworkId)

        userApi.postLikeByUser(likeRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("UserService", "Like added successfully")
                    likeLiveData.value = true // Indica éxito
                } else {
                    Log.e("UserService", "Error adding like: ${response.code()}")
                    likeLiveData.value = false // Indica fallo
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UserService", "Failure adding like: ${t.message}")
                likeLiveData.value = false
            }
        })

        return likeLiveData
    }

    fun getLikesByUser(userId: Int): MutableLiveData<List<ArtworkResponse>> {
        val likedArtworksLiveData = MutableLiveData<List<ArtworkResponse>>()

        userApi.getLikesByUser(userId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { likedArtworks ->
                        Log.d("UserService", "Fetched liked artworks: $likedArtworks")
                        likedArtworksLiveData.value = likedArtworks // Lista de obras que le gustan al usuario
                    } ?: run {
                        Log.e("UserService", "Empty response for liked artworks")
                        likedArtworksLiveData.value = emptyList() // En caso de que no haya obras
                    }
                } else {
                    Log.e("UserService", "Error fetching liked artworks: ${response.code()}")
                    likedArtworksLiveData.value = emptyList() // Manejo de error
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("UserService", "Failure fetching liked artworks: ${t.message}")
                likedArtworksLiveData.value = emptyList()
            }
        })

        return likedArtworksLiveData
    }

    fun deleteLikeByUser(userId: Int, artworkId: Int): MutableLiveData<Boolean> {
        val unlikeLiveData = MutableLiveData<Boolean>()

        userApi.deleteLikeByUser(userId, artworkId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("UserService", "Like removed successfully")
                    unlikeLiveData.value = true // Indica éxito
                } else {
                    Log.e("UserService", "Error removing like: ${response.code()}")
                    unlikeLiveData.value = false // Indica fallo
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("UserService", "Failure removing like: ${t.message}")
                unlikeLiveData.value = false
            }
        })

        return unlikeLiveData
    }
}