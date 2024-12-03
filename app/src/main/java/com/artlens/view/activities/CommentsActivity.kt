package com.artlens.view.activities

import android.content.ContentValues.TAG
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.services.NetworkReceiver
import com.artlens.utils.UserPreferences
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.view.composables.CommentsScreen
import com.artlens.view.composables.NoInternetScreen
import com.artlens.view.viewmodels.CommentsViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentsActivity : ComponentActivity() {

    private val commentsViewModel: CommentsViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        val user = hashMapOf(
            "Count" to "1",
            "Fecha" to Timestamp.now()
        )

        db.collection("BQ37")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        // Configurar el BroadcastReceiver para monitorear la conexión a internet
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // Obtener el ID de la obra desde el Intent
        val artworkId = intent.getIntExtra("ARTWORK_ID", -1)

        // Validar el ID
        if (artworkId == -1) {
            Log.e("CommentsActivity", "Invalid artwork ID")
            finish() // Cierra la actividad si el ID no es válido
            return
        }

        val userId = UserPreferences.getPk() ?: -1

        setContent {

            if (isConnected ) {
                val commentsState by commentsViewModel.commentsLiveData.observeAsState(emptyList())

                CommentsScreen(
                    comments = commentsState,
                    onBackClick = { onBackPressed() },
                    isLoggedIn = userId >= 0,
                    onPostComment = { content ->
                        commentsViewModel.postComment(
                            content = content,
                            date = "2024-12-02", // Fecha fija para pruebas (puedes generar dinámicamente la fecha actual)
                            artworkId = artworkId,
                            userId = userId //
                        )
                    }
                )


                // Llama al ViewModel para obtener los comentarios
                LaunchedEffect(Unit) {
                    Log.d("CommentsActivity", "Fetching comments for artwork ID: $artworkId")
                    commentsViewModel.fetchCommentsByArtwork(artworkId)
                }
            }else {
                // Mostrar pantalla de espera si no hay conexión
                NoInternetScreen()
            }

        }
    }
}
