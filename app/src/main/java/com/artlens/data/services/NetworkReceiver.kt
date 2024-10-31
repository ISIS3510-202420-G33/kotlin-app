package com.artlens.data.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkReceiver(private val onNetworkChange: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val isConnected = NetworkUtils.isInternetAvailable(it)
            onNetworkChange(isConnected)
        }
    }
}
