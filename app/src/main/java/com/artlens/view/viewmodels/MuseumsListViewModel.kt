package com.artlens.view.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.MuseumResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MuseumsListViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private val _museumsLiveData = MutableLiveData<List<MuseumResponse>>()
    val museumsLiveData: LiveData<List<MuseumResponse>> = _museumsLiveData

    private val _closestMuseums = MutableLiveData<List<MuseumResponse>>()
    val closestMuseums: LiveData<List<MuseumResponse>> = _closestMuseums

    init {
        fetchMuseums()
    }

    fun fetchMuseums() {
        viewModelScope.launch(Dispatchers.IO) {
            val museums = facade.getAllMuseums()
            withContext(Dispatchers.Main) {
                museums?.let {
                    Log.d("MuseumsListViewModel", "Museums received: ${it.size}")
                    _museumsLiveData.value = it
                } ?: Log.e("MuseumsListViewModel", "No museums found")
            }
        }
    }

    fun getClosestMuseums(userLat: Double, userLng: Double): LiveData<List<MuseumResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("MuseumsListViewModel", "Fetching closest museums for user location: $userLat, $userLng")
            val museums = _museumsLiveData.value.orEmpty()

            Log.d("MuseumsListViewModel", "Total museums available: ${museums.size}")

            // Ordenamos los museos por distancia y tomamos los 5 más cercanos
            val sortedMuseums = museums.sortedBy {
                val distance = distanceBetween(userLat, userLng, it.fields.latitude, it.fields.longitude)
                Log.d("MuseumsListViewModel", "Distance to museum ${it.fields.name}: $distance")
                distance
            }.take(1)

            Log.d("MuseumsListViewModel", "Closest museums after sorting: ${sortedMuseums.map { it.fields.name }}")

            // Actualizamos el LiveData en el hilo principal
            _closestMuseums.postValue(sortedMuseums)
        }
        return closestMuseums
    }

    private fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Radio de la tierra en kilómetros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c
    }
}