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
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnterPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "EnterPasswordViewModel"
    }

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun signIn(username: String, password: String, onResult: (Boolean) -> Unit) {
        AWSMobileClient.getInstance().signIn(username, password, null, object : Callback<SignInResult> {
            override fun onResult(signInResult: SignInResult) {
                val tokens = AWSMobileClient.getInstance().tokens
                userRepository.setLoggedIn(tokens.accessToken.toString())
                onResult(true)
            }

            override fun onError(e: Exception) {
                onResult(false)
            }
        })
    }
}