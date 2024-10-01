package com.artlens.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.facade.ViewModelFactory
import com.artlens.view.composables.CreateAccountScreen
import com.artlens.view.composables.LogInScreen
import com.artlens.view.viewmodels.CreateAccountViewModel

class CreateAccountActivity : ComponentActivity() {

    private val createAccountViewModel: CreateAccountViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CreateAccountScreen(
                onBackClick = {navigateToLogIn()},
                onHomeClick = {navigateToMainActivity()},
                onRecommendationClick = {navigateToRecommendations()},
                onCreateAccount = {email, userName, name, password ->
            createUser(email, userName, name, password)},
                userResponse = createAccountViewModel.userResponse
            )
        }
    }


    private fun createUser(email: String, userName: String, name: String, password: String) {

        createAccountViewModel.createUser(email, userName, name, password)

    }

    private fun navigateToMainActivity() {
        createAccountViewModel.clearInfo()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRecommendations() {
        createAccountViewModel.clearInfo()
        val intent = Intent(this, RecommendationsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogIn() {
        createAccountViewModel.clearInfo()
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

}