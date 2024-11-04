package com.artlens.view.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artlens.view.composables.LikesListScreen
import com.artlens.view.viewmodels.LikesViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore

class ListScreenActivity : ComponentActivity() {

    // Obtenemos el ViewModel de los museos que le gustan al usuario
    private val likesViewModel: LikesViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "Funcionalidad" to "Fun2",
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

