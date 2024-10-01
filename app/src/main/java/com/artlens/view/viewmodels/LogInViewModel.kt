package com.artlens.view.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.artlens.data.facade.ArtlensFacade
import com.artlens.data.models.CreateUserResponse

class LogInViewModel(private val facade: ArtlensFacade) : ViewModel() {

    private var _userResponse = MutableLiveData<CreateUserResponse>()
    val userResponse: LiveData<CreateUserResponse> get() = _userResponse

    fun authenticateUser(userName: String, password: String) {

        facade.authenticateUser(userName, password).observeForever { response ->
            // Assuming response is of type CreateUserResponse
            _userResponse.value = response // Update LiveData with the response

        }

    }

    fun clearInfo() {
        _userResponse = MutableLiveData<CreateUserResponse>()
    }



}