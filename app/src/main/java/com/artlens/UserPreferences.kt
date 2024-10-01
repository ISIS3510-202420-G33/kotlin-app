package com.artlens.utils

import android.content.Context
import android.content.SharedPreferences
import com.artlens.ArtLensApp

object UserPreferences {

    private const val PREFERENCES_NAME = "UserPrefs"
    private const val USERNAME_PK = "pk"
    private const val USERNAME_KEY = "username"
    private const val EMAIL_KEY = "email"

    // Get SharedPreferences instance
    private fun getSharedPreferences(): SharedPreferences {
        return ArtLensApp.getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    // Save User Data
    fun saveUser(pk: Int, username: String, email: String) {
        val sharedPreferences = getSharedPreferences()
        sharedPreferences.edit().apply {
            putInt(USERNAME_PK, pk)
            putString(USERNAME_KEY, username)
            putString(EMAIL_KEY, email)
            apply()
        }
    }

    // Retrieve User Data
    fun getUsername(): String? {
        return getSharedPreferences().getString(USERNAME_KEY, null)
    }

    fun getEmail(): String? {
        return getSharedPreferences().getString(EMAIL_KEY, null)
    }

    fun getPk(): Int? {
        return getSharedPreferences().getInt(USERNAME_PK, -1)
    }

    // Clear User Data
    fun clearUserData() {
        val sharedPreferences = getSharedPreferences()
        sharedPreferences.edit().clear().apply()
    }
}
