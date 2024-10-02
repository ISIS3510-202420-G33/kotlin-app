package com.artlens.view.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.CreateUserResponse
import kotlinx.coroutines.launch

class CreateAccountViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private var _userResponse = MutableLiveData<CreateUserResponse>()
    val userResponse: LiveData<CreateUserResponse> get() = _userResponse

    fun createUser(email: String, userName: String, name: String, password: String) {

        facade.createUser(email, userName, name, password).observeForever { response ->
            // Assuming response is of type CreateUserResponse
            _userResponse.value = response // Update LiveData with the response
            
        }

    }

    fun clearInfo() {

        _userResponse = MutableLiveData<CreateUserResponse>()

    }

}