package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.artlens.view.composables.RecommendationsScreen

class RecommendationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecommendationsScreen(
                onBackClick = {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}
