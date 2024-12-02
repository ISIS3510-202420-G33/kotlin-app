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
import com.artlens.view.composables.LogInScreen
import com.artlens.view.composables.NoInternetScreen
import com.artlens.view.composables.NoInternetScreenV2
import com.artlens.view.viewmodels.LogInViewModel

class LogInActivity : ComponentActivity() {

    private lateinit var networkReceiver: NetworkReceiver
    private var isConnected by mutableStateOf(true)

    private val logInViewModel: LogInViewModel by viewModels {
        ViewModelFactory(FacadeProvider.facade)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkReceiver = NetworkReceiver { isConnected = it }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

            setContent {
                LaunchedEffect(Unit) {
                    isConnected = NetworkUtils.isInternetAvailable(this@LogInActivity)
                }
                if (isConnected) {
                    LogInScreen(
                        onCreateAccount = { navigateToCreate() },
                        onBackClick = { onBackPressed() },
                        onLogInClick = { userName, password ->
                            authenticateUser(
                                userName,
                                password
                            )
                        },
                        userResponse = logInViewModel.userResponse
                    )
                }
                    else{
                        NoInternetScreenV2(
                            onBackClick = { onBackPressed() },
                            textTop = "LOGIN"
                        )
            }
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

    override fun onDestroy() {
        super.onDestroy()
        // Desregistrar el BroadcastReceiver para evitar fugas de memoria
        unregisterReceiver(networkReceiver)
    }

}