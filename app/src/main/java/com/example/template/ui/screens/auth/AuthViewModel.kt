package com.example.template.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.template.networking.JSONPlaceholder
import com.example.template.networking.Lambdas
import com.example.template.utils.Constants.AWS_COGNITO_USER_DOES_EXIST_MESSAGE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

    suspend fun checkIfUserExists(username: String): Boolean = suspendCancellableCoroutine { continuation ->
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = Lambdas.api.checkIfUserExists(username.lowercase())
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == AWS_COGNITO_USER_DOES_EXIST_MESSAGE) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    }
                } else {
                    continuation.resume(false)
                }
            } catch (e: Exception) {
                // Handle exception
                continuation.resumeWithException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}