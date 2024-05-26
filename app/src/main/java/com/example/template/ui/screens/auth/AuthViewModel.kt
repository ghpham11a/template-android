package com.example.template.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignUpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.delay
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient
import com.amazonaws.services.cognitoidentityprovider.model.AdminGetUserRequest
import com.amazonaws.services.cognitoidentityprovider.model.AdminGetUserResult

class AuthViewModel: ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    suspend fun checkIfUserExists(username: String): Boolean {

        _isLoading.value = true

        val users = listOf("gm.pham@gmail.com").toSet()

        delay(1000)

        _isLoading.value = false

        return users.contains(username)

    }
}