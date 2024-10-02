package com.artlens.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.viewmodels.ArtworkViewModel
import com.artlens.view.composables.ArtworkDetailScreen
import java.util.*

class ArtworkDetailActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val artworkViewModel: ArtworkViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var tts: TextToSpeech  // Text-to-Speech instance

    companion object {
        private const val DEFAULT_USER_ID = 1
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos TTS
        tts = TextToSpeech(this, this)

        // Obtener el ID de la obra que queremos mostrar
        val artworkId = intent.getIntExtra("id", 2)

        // Usar el ID de usuario por defecto
        val userId = DEFAULT_USER_ID

        setContent {
            val artworkState by artworkViewModel.artworkLiveData.observeAsState()
            val isLiked by artworkViewModel.isLiked.observeAsState(false)

            // Llamar al ViewModel para obtener los detalles de la obra
            LaunchedEffect(Unit) {
                artworkViewModel.fetchArtworkDetail(artworkId, userId)
            }

            // Configurar la estructura general de la pantalla con Scaffold
            Scaffold {
                // Mostrar la pantalla de detalle de la obra
                ArtworkDetailScreen(
                    artwork = artworkState,
                    isLiked = isLiked,
                    onBackClick = { onBackPressed() },
                    onLikeClick = {
                        artworkViewModel.toggleLike(userId, artworkId)
                    },
                    onMoreInfoClick = { artistId ->
                        val intent = Intent(this@ArtworkDetailActivity, ArtistDetailActivity::class.java)
                        intent.putExtra("ARTIST_ID", artistId)
                        startActivity(intent)
                    },
                    onInterpretationSpeakClick = { interpretation ->
                        speakOut(interpretation)  // Leer en voz alta la interpretación
                    }
                )
            }
        }
    }

    // Inicializar el Text-to-Speech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Maneja el error si el idioma no está soportado
            }
        } else {
            // Manejar error de inicialización
        }
    }

    // Método para leer el texto en voz alta
    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Liberar recursos cuando se destruya la actividad
    override fun onDestroy() {
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
