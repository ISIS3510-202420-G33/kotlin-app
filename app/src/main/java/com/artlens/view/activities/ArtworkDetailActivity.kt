package com.artlens.view.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier // Import correcto para Modifier
import com.artlens.data.facade.FacadeProvider
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.utils.UserPreferences
import com.artlens.view.viewmodels.ArtworkViewModel
import com.artlens.view.composables.ArtworkDetailScreen
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ArtworkDetailActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val artworkViewModel: ArtworkViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var tts: TextToSpeech
    private var isSpeaking by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)
        val artworkId = intent.getIntExtra("id", 2)
        val userId = UserPreferences.getPk() ?: -1

        artworkViewModel.fetchArtworkDetail(artworkId, userId)

        setContent {
            val artworkState by artworkViewModel.artworkLiveData.observeAsState()
            val isLiked by artworkViewModel.isLiked.observeAsState(false)
            val artistName by artworkViewModel.artistName.observeAsState()

            LaunchedEffect(artworkState) {
                artworkState?.let { artwork ->
                    artwork.fields.artist?.let { artistId ->
                        artworkViewModel.fetchArtistName(artistId)
                    }
                }
            }

            Scaffold { paddingValues ->
                // Pasar paddingValues a ArtworkDetailScreen para que use el espacio de manera adecuada
                ArtworkDetailScreen(
                    artwork = artworkState,
                    isLiked = isLiked,
                    artistName = artistName,
                    isSpeaking = isSpeaking,
                    onBackClick = { onBackPressed() },
                    onLikeClick = {
                        if (userId != -1) {
                            artworkViewModel.toggleLike(userId, artworkId)
                        }
                    },
                    onMoreInfoClick = { artistId ->
                        val intent = Intent(this@ArtworkDetailActivity, ArtistDetailActivity::class.java)
                        intent.putExtra("ARTIST_ID", artistId)
                        startActivity(intent)
                    },
                    onInterpretationSpeakClick = { interpretation ->
                        toggleTTS(interpretation)
                    },
                    onCrashButtonClick = { registerCrash() },
                    modifier = Modifier.padding(paddingValues) // Añadir el padding aquí
                )
            }
        }
    }

    private fun registerCrash() {
        val db = Firebase.firestore
        val crashData = hashMapOf(
            "crashCount" to Timestamp.now(),
            "androidVersion" to Build.VERSION.RELEASE
        )

        db.collection("BQ11")  // Colección para registrar crashes
            .add(crashData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Crash report added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding crash report", e)
            }

        // Simulación de un crash para pruebas
        throw RuntimeException("Simulated crash for testing")
    }

    // Inicializar el Text-to-Speech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.ENGLISH
        }
    }

    private fun toggleTTS(text: String) {
        if (isSpeaking) {
            stopTTS()
        } else {
            speakOut(text)
        }
    }

    private fun speakOut(text: String) {
        val db = Firebase.firestore
        db.collection("BQ33")
            .add(hashMapOf("Funcionalidad" to "Fun4", "Fecha" to Timestamp.now()))
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        isSpeaking = true
    }

    private fun stopTTS() {
        if (this::tts.isInitialized) {
            tts.stop()
            isSpeaking = false
        }
    }

    override fun onDestroy() {
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
