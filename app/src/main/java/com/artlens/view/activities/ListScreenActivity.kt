package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artlens.view.composables.LikesListScreen
import com.artlens.view.viewmodels.LikesViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.utils.UserPreferences

class ListScreenActivity : ComponentActivity() {

    // Obtenemos el ViewModel de los museos que le gustan al usuario
    private val likesViewModel: LikesViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id  = UserPreferences.getPk()

        if (id != null) {
            likesViewModel.fetchLikedMuseums(userId = id)
        }

        setContent {
            LikesListScreen(
                onBackClick = {
                    onBackPressed()
                },
                onMuseumClick = { artworkId ->
                    // Navegar al detalle de la obra de arte cuando se haga clic en una tarjeta
                    val intent = Intent(this, ArtworkDetailActivity::class.java)
                    intent.putExtra("id", artworkId)  // Pasamos el ID de la obra seleccionada
                    startActivity(intent)
                },
                likesViewModel = likesViewModel
            )
        }
    }
}

