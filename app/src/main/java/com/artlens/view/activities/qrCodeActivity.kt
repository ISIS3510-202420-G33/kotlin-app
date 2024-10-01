package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.artlens.view.composables.QRCodeScanner


class qrCodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var qrCodeValue by remember { mutableStateOf("") }

            QRCodeScanner(
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

    private fun artDetail(id: String){
        val intent = Intent(this, ArtworkDetailActivity::class.java)
        intent.putExtra("id", id.toInt())
        startActivity(intent)
    }

}




