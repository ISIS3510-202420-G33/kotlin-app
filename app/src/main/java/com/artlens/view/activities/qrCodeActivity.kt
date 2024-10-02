package com.artlens.view.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.artlens.utils.UserPreferences
import com.artlens.view.composables.QRCodeScanner
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import com.google.firebase.Timestamp


class qrCodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var qrCodeValue by remember { mutableStateOf("") }

            val db = Firebase.firestore

            // Create a new user with a first, middle, and last name
            val user = hashMapOf(
                "Funcionalidad" to "Fun1",
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




