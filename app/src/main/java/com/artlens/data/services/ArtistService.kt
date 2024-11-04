package com.artlens.data.services

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.ArtistApi
import com.artlens.data.models.ArtistResponse
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistService(private val artistApi: ArtistApi) {

    suspend fun getArtistDetail(artistId: Int): ArtistResponse? {

        val db = Firebase.firestore

        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "Funcionalidad" to "Fun6",
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

        return try {
            val response = artistApi.getArtistDetail(artistId)
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                response.body()?.first() // Devuelve el primer ArtistResponse si existe
            } else {
                Log.e("ArtistService", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ArtistService", "Network failure: ${e.message}")
            null
        }
    }
    fun getAllArtists(): LiveData<List<ArtistResponse>> {
        val artistsLiveData = MutableLiveData<List<ArtistResponse>>()

        artistApi.getAllArtists().enqueue(object : Callback<List<ArtistResponse>> {
            override fun onResponse(call: Call<List<ArtistResponse>>, response: Response<List<ArtistResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    artistsLiveData.value = response.body()
                } else {
                    Log.e("ArtistService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtistResponse>>, t: Throwable) {
                artistsLiveData.value = emptyList()  // Retornamos una lista vacía si falla
                Log.e("ArtistService", "Failure: ${t.message}")
            }
        })

        return artistsLiveData
    }
}
