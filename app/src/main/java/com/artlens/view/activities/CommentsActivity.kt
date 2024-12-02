package com.artlens.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.view.composables.CommentsScreen
import com.artlens.view.viewmodels.CommentsViewModel

class CommentsActivity : ComponentActivity() {

    private val commentsViewModel: CommentsViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ID de la obra desde el Intent
        val artworkId = intent.getIntExtra("ARTWORK_ID", -1)

        // Validar el ID
        if (artworkId == -1) {
            Log.e("CommentsActivity", "Invalid artwork ID")
            finish() // Cierra la actividad si el ID no es válido
            return
        }

        setContent {
            val commentsState by commentsViewModel.commentsLiveData.observeAsState(emptyList())

            CommentsScreen(
                comments = commentsState,
                onBackClick = { onBackPressed() },
                onPostComment = { content ->
                    commentsViewModel.postComment(
                        content = content,
                        date = "2024-12-02", // Fecha fija para pruebas (puedes generar dinámicamente la fecha actual)
                        artworkId = artworkId,
                        userId = 1 // Reemplaza con el ID real del usuario si está disponible
                    )
                }
            )


            // Llama al ViewModel para obtener los comentarios
            LaunchedEffect(Unit) {
                Log.d("CommentsActivity", "Fetching comments for artwork ID: $artworkId")
                commentsViewModel.fetchCommentsByArtwork(artworkId)
            }
        }
    }
}
