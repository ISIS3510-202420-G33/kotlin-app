package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.view.composables.MuseumsListScreen
import com.artlens.view.viewmodels.MuseumsListViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory

class MuseumsListActivity : ComponentActivity() {

    private val museumsViewModel: MuseumsListViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Observar el estado de los museos desde el ViewModel
            val museumsState by museumsViewModel.museumsLiveData.observeAsState(emptyList()) // Observa la lista de museos

            // Pasar la lista de museos y el manejador del click a la pantalla de lista
            MuseumsListScreen(museums = museumsState) { museumId ->
                // Crear un intent para ir a MuseumsDetailActivity con el ID del museo seleccionado
                val intent = Intent(this, MuseumsDetailActivity::class.java)
                intent.putExtra("MUSEUM_ID", museumId)
                startActivity(intent)
            }
        }
    }
}
