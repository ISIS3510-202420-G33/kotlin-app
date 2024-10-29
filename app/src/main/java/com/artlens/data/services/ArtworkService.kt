package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.ArtworkApi
import com.artlens.data.cache.ArtworkCache
import com.artlens.data.models.ArtworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtworkService(private val artworkApi: ArtworkApi, private val cache: ArtworkCache) {

    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse> {
        val artworkLiveData = MutableLiveData<ArtworkResponse>()


        // Check the cache first
        cache.get(artworkId)?.let {
            // Return cached data if available

            Log.e("AAAAAAAAAAAAAAAAAAAAAAAA", "Cache utilizadoooo")
            artworkLiveData.value = it
            return artworkLiveData
        }


        // If not cached, proceed with network request
        artworkApi.getArtworkDetail(artworkId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val artwork = response.body()?.first()

                    // Save to cache and update LiveData
                    if (artwork != null) {
                        cache.put(artworkId, artwork)
                        artworkLiveData.value = artwork
                    }
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                artworkLiveData.value = null
                Log.e("ArtworkService", "Failure: ${t.message}")
            }
        })

        return artworkLiveData
    }

    // Nuevo método para obtener las primeras 5 obras de arte
    fun getAllArtworks(): LiveData<List<ArtworkResponse>> {
        val artworksLiveData = MutableLiveData<List<ArtworkResponse>>()

        artworkApi.getAllArtworks().enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Solo obtenemos las primeras 5 obras de arte
                    artworksLiveData.value = response.body()?.take(20)
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("ArtworkService", "Failure: ${t.message}")
                artworksLiveData.value = emptyList() // Lista vacía en caso de error
            }
        })

        return artworksLiveData
    }

    fun getArtworksByArtist(artistId: Int): LiveData<List<ArtworkResponse>> {
        val artworksLiveData = MutableLiveData<List<ArtworkResponse>>()

        artworkApi.getArtworksByArtist(artistId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Obtenemos las primeras 20 obras de arte del artista
                    artworksLiveData.value = response.body()?.take(20)
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("ArtworkService", "Failure: ${t.message}")
                artworksLiveData.value = emptyList() // Lista vacía en caso de error
            }
        })

        return artworksLiveData
    }

    fun getArtworksByMuseum(museumId: Int): LiveData<List<ArtworkResponse>> {
        val artworksLiveData = MutableLiveData<List<ArtworkResponse>>()

        artworkApi.getArtworksByMuseum(museumId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Obtenemos las primeras 20 obras de arte del museo
                    artworksLiveData.value = response.body()?.take(20)
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("ArtworkService", "Failure: ${t.message}")
                artworksLiveData.value = emptyList() // Lista vacía en caso de error
            }
        })

        return artworksLiveData
    }
}

