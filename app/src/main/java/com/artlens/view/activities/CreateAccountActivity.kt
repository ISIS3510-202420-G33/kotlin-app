package com.artlens.view.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.artlens.data.facade.FacadeProvider
import com.artlens.data.services.NetworkReceiver
import com.artlens.data.services.NetworkUtils
import com.artlens.view.viewmodels.ViewModelFactory
import com.artlens.view.composables.CreateAccountScreen
import com.artlens.view.composables.NoInternetScreenV2
import com.artlens.view.viewmodels.CreateAccountViewModel

class CreateAccountActivity : ComponentActivity() {

    private val createAccountViewModel: CreateAccountViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        setContent {
            LaunchedEffect(Unit) {
                isConnected = NetworkUtils.isInternetAvailable(this@CreateAccountActivity)
            }
            if (isConnected) {
            CreateAccountScreen(
                onBackClick = {onBackPressed()},
                onHomeClick = {navigateToMainActivity()},
                onRecommendationClick = {navigateToRecommendations()},
                onCreateAccount = {email, userName, name, password ->
            createUser(email, userName, name, password)},
                userResponse = createAccountViewModel.userResponse
            )}
            else{
                NoInternetScreenV2(
                    onBackClick = { onBackPressed() },
                    textTop = "LOGIN"
                )
            }
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