package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.MuseumResponse

class MuseumsListViewModel(private val facade: ArtlensFacade) : ViewModel() {

    // LiveData que contiene la lista de museos
    val museumsLiveData: LiveData<List<MuseumResponse>> = facade.getAllMuseums()
}
