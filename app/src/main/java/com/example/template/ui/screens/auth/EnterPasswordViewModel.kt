package com.example.template.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.Tokens
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.example.template.models.AWSMobileClientResponse
import com.example.template.models.AdminUpdateUserBody
import com.example.template.networking.APIGateway
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

    private val _password = MutableStateFlow("ABcd1234$$")
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
                val response = APIGateway.api.adminEnableUser(
                    APIGateway.buildHeaders(),
                    username,
                    AdminUpdateUserBody("enable", username)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message?.contains("enabled successfully") == true) {
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
                    userRepository.setLoggedIn(AWSMobileClient.getInstance().tokens, username, AWSMobileClient.getInstance().userAttributes ?: emptyMap())
                }
                getUserAttributes()
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

    private fun getUserAttributes() {
        AWSMobileClient.getInstance().getTokens(object : Callback<Tokens> {
            override fun onResult(result: Tokens?) {
                AWSMobileClient.getInstance().getUserAttributes(object : Callback<Map<String, String>> {
                    override fun onResult(userAttributes: Map<String, String>?) {
                        if (userAttributes != null) {
                            // Use the userAttributes map which contains all the Cognito data for the user
                            for ((key, value) in userAttributes) {
                                Log.d("UserAttribute", "$key: $value")
                            }
                        }
                    }

                    override fun onError(e: Exception?) {
                        Log.e("GetUserAttributes", "Error getting user attributes", e)
                    }
                })
            }

            override fun onError(e: Exception?) {
                Log.e("GetTokens", "Error getting tokens", e)
            }
        })
    }
}