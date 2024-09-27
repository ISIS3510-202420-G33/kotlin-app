package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.artlens.view.composables.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                onArtistDetailClick = {
                    startActivity(Intent(this, ArtistDetailActivity::class.java))
                },
                onArtworkDetailClick = {
                    startActivity(Intent(this, ArtworkDetailActivity::class.java))
                }
            )
        }
    }
}
