package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.artlens.utils.UserPreferences
import com.artlens.view.composables.QRCodeScanner


class qrCodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var qrCodeValue by remember { mutableStateOf("") }

            QRCodeScanner(
                onBackClick = {navigateToMainActivity()},
                onHomeClick = {navigateToMainActivity()},
                onRecommendationClick = {navigateToRecommendations()},
                onUSerClick = {navigateUser()},
                onQRCodeScanned = { qrCode ->
                    qrCodeValue = qrCode
                }
            )

            if (qrCodeValue.isNotEmpty()) {
                // Do something with the scanned QR code
                artDetail(qrCodeValue)
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRecommendations() {
        val intent = Intent(this, RecommendationsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogIn() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

    private fun artDetail(id: String){
        val intent = Intent(this, ArtworkDetailActivity::class.java)
        intent.putExtra("id", id.toInt())
        startActivity(intent)
    }

    private fun navigateUser() {
        val pk = UserPreferences.getPk()
        if(pk!!>=0) {
            //Poner Lista de likeados
        }
        else{
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }

}




