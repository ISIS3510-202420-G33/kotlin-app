package com.artlens.view.activities

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import com.artlens.view.composables.LikesListScreen
import com.artlens.view.viewmodels.LikesViewModel
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.models.ArtworkResponse
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import java.io.OutputStream

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
            val context = LocalContext.current
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
                onDownloadClick = { likedMuseums ->
                    // Pass the liked museums list to downloadFavorites
                    downloadFavorites(likedMuseums, context)
                },
                likesViewModel = likesViewModel
            )
        }
    }



    private fun downloadFavorites(likedMuseums: List<ArtworkResponse>, context: Context) {
        val fileName = "favorite_museums.txt"


        // Use MediaStore for Android 10+
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        if (uri != null) {
            try {
                resolver.openOutputStream(uri).use { outputStream ->
                    writeFavoritesToStream(likedMuseums, outputStream)
                }
                Toast.makeText(context, "File saved to Downloads folder.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error saving file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Failed to create file.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeFavoritesToStream(likedMuseums: List<ArtworkResponse>, outputStream: OutputStream?) {
        outputStream?.bufferedWriter()?.use { writer ->
            likedMuseums.forEach { artwork ->
                val museumData = "Name: ${artwork.fields.name}\nDescription: ${artwork.fields.advancedInfo}\n\n"
                writer.write(museumData)
            }
        }
    }
}

