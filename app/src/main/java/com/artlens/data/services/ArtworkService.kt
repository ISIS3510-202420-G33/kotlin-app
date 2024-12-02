package com.artlens.data.services

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artlens.data.api.ArtworkApi
import com.artlens.data.cache.ArtworkCache
import com.artlens.data.models.ArtworkResponse
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.OutputStream

class ArtworkService(private val artworkApi: ArtworkApi, private val cache: ArtworkCache) {

    fun getArtworkDetail(artworkId: Int): LiveData<ArtworkResponse> {
        val artworkLiveData = MutableLiveData<ArtworkResponse>()



        // Check the cache first
        cache.get(artworkId)?.let {
            // Return cached data if available

            Log.e("AAAAAAAAAAAAAAAAAAAAAAAA", "Cache utilizadoooo")
            artworkLiveData.value = it
            return artworkLiveData
        }

        val db = Firebase.firestore
        val startTime = System.currentTimeMillis()

        // If not cached, proceed with network request
        artworkApi.getArtworkDetail(artworkId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                val elapsedTime = System.currentTimeMillis() - startTime
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val artwork = response.body()?.first()

                    // Save to cache and update LiveData
                    if (artwork != null) {
                        cache.put(artworkId, artwork)
                        artworkLiveData.value = artwork
                    }
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }

                // Create a new user with a first, middle, and last name
                val user = hashMapOf(
                    "Tiempo" to elapsedTime,
                    "Fecha" to Timestamp.now()
                )

                // Add a new document with a generated ID
                db.collection("BQ12")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }


            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                val elapsedTime = System.currentTimeMillis() - startTime
                artworkLiveData.value = null
                Log.e("ArtworkService", "Failure: ${t.message}")


                // Create a new user with a first, middle, and last name
                val user = hashMapOf(
                    "Tiempo" to elapsedTime,
                    "Fecha" to Timestamp.now()
                )

                // Add a new document with a generated ID
                db.collection("BQ12")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        })

        return artworkLiveData
    }

    // Nuevo método para obtener las primeras 5 obras de arte
    fun getAllArtworks(): LiveData<List<ArtworkResponse>> {
        val artworksLiveData = MutableLiveData<List<ArtworkResponse>>()

        artworkApi.getAllArtworks().enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Solo obtenemos las primeras 5 obras de arte
                    artworksLiveData.value = response.body()?.take(20)
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("ArtworkService", "Failure: ${t.message}")
                artworksLiveData.value = emptyList() // Lista vacía en caso de error
            }
        })

        return artworksLiveData
    }

    fun getArtworksByArtist(artistId: Int): LiveData<List<ArtworkResponse>> {
        val artworksLiveData = MutableLiveData<List<ArtworkResponse>>()

        artworkApi.getArtworksByArtist(artistId).enqueue(object : Callback<List<ArtworkResponse>> {
            override fun onResponse(call: Call<List<ArtworkResponse>>, response: Response<List<ArtworkResponse>>) {
                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    // Obtenemos las primeras 20 obras de arte del artista
                    artworksLiveData.value = response.body()?.take(20)
                } else {
                    Log.e("ArtworkService", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ArtworkResponse>>, t: Throwable) {
                Log.e("ArtworkService", "Failure: ${t.message}")
                artworksLiveData.value = emptyList() // Lista vacía en caso de error
            }
        })

        return artworksLiveData
    }

    suspend fun getArtworksByMuseum(museumId: Int): List<ArtworkResponse> {
        return try {
            val response = artworkApi.getArtworksByMuseum(museumId)

            if (response.isSuccessful && response.body() != null) {
                response.body() ?: emptyList() // Devuelve la lista de recomendaciones
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("AnalyticsService", "Network failure: ${e.message}")
            emptyList() // Devuelve una lista vacía en caso de fallo de red
        }
    }


    fun downloadFavorites(likedMuseums: List<ArtworkResponse>, context: Context) {
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
                val museumData = "Name: ${artwork.fields.name}\n" +
                        "\tDate: ${artwork.fields.date}\n" +
                        "\tTechnique: ${artwork.fields.technique}\n" +
                        "\tDimensions: ${artwork.fields.dimensions}\n" +
                        "\tInterpretation: ${artwork.fields.interpretation}\n" +
                        "\tDescription: ${artwork.fields.advancedInfo}\n\n"
                writer.write(museumData)
            }
        }
    }


}

