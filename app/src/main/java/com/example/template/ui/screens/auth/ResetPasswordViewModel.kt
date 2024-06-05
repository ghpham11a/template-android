package com.example.template.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.ForgotPasswordResult
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
class ResetPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "ResetPasswordViewModel"
    }

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onUsernameChange(newPassword: String) {
        _username.value = newPassword
    }

    fun resetPassword(username: String, onResult: (AWSMobileClientResponse<ForgotPasswordResult>) -> Unit) {

        _isLoading.value = true

        AWSMobileClient.getInstance().forgotPassword(username.lowercase(), object : Callback<ForgotPasswordResult> {
            override fun onResult(result: ForgotPasswordResult) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(true, result, null))
            }

            override fun onError(e: Exception) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(false, null, e))
            }
        })
    }
}