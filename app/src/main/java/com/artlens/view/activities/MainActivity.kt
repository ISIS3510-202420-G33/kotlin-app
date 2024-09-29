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
                onMapClick = {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                },
                onMuseumClick = {
                    val intent = Intent(this, MuseumsListActivity::class.java)
                    startActivity(intent)
                },
                onRecommendationClick = {
                    val intent = Intent(this, RecommendationsActivity::class.java)
                    startActivity(intent)
                },

                onBackClick = {
                    // No hacer nada cuando se presiona la flecha de retroceso
                }
            )
        }
    }
}

