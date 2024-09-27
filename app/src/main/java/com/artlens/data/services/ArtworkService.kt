package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.ArtworkApi
import com.artlens.data.models.ArtworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtworkService(private val artworkApi: ArtworkApi) {

    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse> {
        val artworkLiveData = MutableLiveData<ArtworkResponse>()

        artworkApi.getArtworkDetail(artworkId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Obtener el primer artwork de la lista
                    artworkLiveData.value = response.body()?.first()
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                artworkLiveData.value = null  // Manejo de error
                Log.e("ArtworkService", "Failure: ${t.message}")
            }
        })

        return artworkLiveData
    }
}

