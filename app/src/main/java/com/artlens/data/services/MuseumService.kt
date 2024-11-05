package com.artlens.data.services

import android.util.Log
import com.artlens.data.api.MuseumApi
import com.artlens.data.cache.MuseumCache
import com.artlens.data.models.MuseumResponse

class MuseumService(private val museumApi: MuseumApi, private val cache: MuseumCache) {

    suspend fun getMuseumDetail(museumId: Int): MuseumResponse? {
        // Primero, intentamos obtener los datos del caché
        cache.get(museumId)?.let {
            Log.d("MuseumService", "Retrieved museum detail from cache")
            return it
        }

        // Si no está en el caché, hacemos la solicitud de red
        return try {
            val response = museumApi.getMuseumDetail(museumId)
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                val museum = response.body()?.first()
                if (museum != null) {
                    cache.put(museumId, museum)  // Guardamos en caché la respuesta
                    museum
                } else {
                    Log.e("MuseumService", "Empty response for museum detail")
                    null
                }
            } else {
                Log.e("MuseumService", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MuseumService", "Network failure: ${e.message}")
            null
        }
    }

    suspend fun getAllMuseums(): List<MuseumResponse>? {
        return try {
            val response = museumApi.getAllMuseums()
            if (response.isSuccessful && response.body() != null) {
                Log.d("MuseumService", "Museums fetched: ${response.body()?.size}")
                response.body()
            } else {
                Log.e("MuseumService", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MuseumService", "Network failure: ${e.message}")
            null
        }
    }
}
