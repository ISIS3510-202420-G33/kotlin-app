package com.artlens

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class ArtLensApp : Application() {

    companion object {
        private lateinit var instance: ArtLensApp

        // Get Application Context
        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}
