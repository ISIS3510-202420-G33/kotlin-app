package com.artlens.data.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.MuseumApi
import com.artlens.data.models.MuseumResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MuseumService(private val museumApi: MuseumApi) {

    fun getMuseumDetail(museumId: Int): LiveData<MuseumResponse> {
        val museumLiveData = MutableLiveData<MuseumResponse>()

        museumApi.getMuseumDetail(museumId).enqueue(object : Callback<List<MuseumResponse>> {
            override fun onResponse(call: Call<List<MuseumResponse>>, response: Response<List<MuseumResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    museumLiveData.value = response.body()?.first()
                } else {
                    // Manejo de error: podrías retornar un valor de error en lugar de null
                    Log.e("MuseumService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MuseumResponse>>, t: Throwable) {
                // No se asigna null directamente, manejo el error de alguna forma
                Log.e("MuseumService", "Failure: ${t.message}")
            }
        })

        return museumLiveData
    }

    fun getAllMuseums(): LiveData<List<MuseumResponse>> {
        val museumLiveData = MutableLiveData<List<MuseumResponse>>()

        museumApi.getAllMuseums().enqueue(object : Callback<List<MuseumResponse>> {
            override fun onResponse(call: Call<List<MuseumResponse>>, response: Response<List<MuseumResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    museumLiveData.value = response.body()
                } else {
                    Log.e("MuseumService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MuseumResponse>>, t: Throwable) {
                Log.e("MuseumService", "Failure: ${t.message}")
                museumLiveData.value = emptyList() // Devolvemos una lista vacía en caso de error
            }
        })

        return museumLiveData
    }
}

