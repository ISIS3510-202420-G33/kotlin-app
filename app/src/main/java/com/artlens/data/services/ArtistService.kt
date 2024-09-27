package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.ArtistApi
import com.artlens.data.models.ArtistResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistService(private val artistApi: ArtistApi) {

    fun getArtistDetail(artistId: Int): LiveData<ArtistResponse> {
        val artistLiveData = MutableLiveData<ArtistResponse>()

        artistApi.getArtistDetail(artistId).enqueue(object : Callback<List<ArtistResponse>> {
            override fun onResponse(call: Call<List<ArtistResponse>>, response: Response<List<ArtistResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Obtener el primer artist de la lista
                    artistLiveData.value = response.body()?.first()
                } else {
                    Log.e("ArtistService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtistResponse>>, t: Throwable) {

                artistLiveData.value = null // Manejo de error
                Log.e("ArtistService", "Failure: ${t.message}")
            }
        })

        return artistLiveData
    }
}
