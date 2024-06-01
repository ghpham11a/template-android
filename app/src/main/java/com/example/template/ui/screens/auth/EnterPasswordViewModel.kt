package com.example.template.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.example.template.models.AWSMobileClientResponse
import com.example.template.models.AdminUpdateUserBody
import com.example.template.networking.Lambdas
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun enableUser(username: String, password: String, onResult: (AWSMobileClientResponse<SignInResult>) -> Unit) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = Lambdas.api.adminEnableUser(
                    Lambdas.buildHeaders(),
                    username,
                    AdminUpdateUserBody("enable", username)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.contains("enabled successfully")) {
                            signIn(username, password, onResult)
                        } else {
                            onResult(AWSMobileClientResponse(false, null, null))
                        }
                    }
                } else {
                    onResult(AWSMobileClientResponse(false, null, null))
                }
            } catch (e: Exception) {
                onResult(AWSMobileClientResponse(false, null, e))
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun signIn(username: String, password: String, onResult: (AWSMobileClientResponse<SignInResult>) -> Unit) {

        _isLoading.value = true

        AWSMobileClient.getInstance().signIn(username, password, null, object : Callback<SignInResult> {
            override fun onResult(signInResult: SignInResult) {
                _isLoading.value = false
                if (signInResult.signInState == SignInState.DONE) {
                    userRepository.setLoggedIn(AWSMobileClient.getInstance().tokens, username)
                }
                onResult(AWSMobileClientResponse(true, signInResult))
            }

            override fun onError(e: Exception) {
                (e as? NotAuthorizedException)?.let {
                    if (it.message?.contains("User is disabled") == true) {
                        enableUser(username, password, onResult)
                    } else {
                        _isLoading.value = false
                        onResult(AWSMobileClientResponse(false, null, e))
                    }
                    enableUser(username, password, onResult)
                } ?: run {
                    _isLoading.value = false
                    onResult(AWSMobileClientResponse(false, null, e))
                }
            }
        })
    }
}