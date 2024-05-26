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
class AddInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "AddInfoViewModel"
    }

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun createAccount(username: String, password: String) {
        val userAttributes = HashMap<String, String>()
        userAttributes["email"] = username
        AWSMobileClient.getInstance().signUp(username, password, userAttributes, null, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                Log.d(TAG, "createAccount: $signUpResult")
            }

            override fun onError(e: Exception) {
                Log.e(TAG, "createAccount: ", e)
            }
        })
    }
}