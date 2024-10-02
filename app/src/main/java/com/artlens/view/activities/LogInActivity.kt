package com.artlens.view.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.composables.LogInScreen
import com.artlens.view.viewmodels.LogInViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore

class LogInActivity : ComponentActivity() {

    private val logInViewModel: LogInViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LogInScreen(
                onCreateAccount = {navigateToCreate()},
                onBackClick = {navigateToMainActivity()},
                onLogInClick = {userName, password -> authenticateUser(userName, password) },
                userResponse = logInViewModel.userResponse
            )
        }
    }

    private fun authenticateUser(userName: String, password: String) {

        logInViewModel.authenticateUser(userName, password)

    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToCreate() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }


}