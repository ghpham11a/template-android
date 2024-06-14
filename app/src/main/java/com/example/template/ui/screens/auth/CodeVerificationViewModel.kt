package com.example.template.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.ForgotPasswordResult
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.SignUpResult
import com.amazonaws.services.cognitoidentityprovider.model.ConfirmForgotPasswordResult
import com.example.template.models.AWSMobileClientResponse
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CodeVerificationViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "CodeVerificationViewModel"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    fun confirmSignUp(username: String, password: String, confirmationCode: String, onResult: (AWSMobileClientResponse<SignInResult>) -> Unit) {
        _isLoading.value = true
        AWSMobileClient.getInstance().confirmSignUp(username, confirmationCode, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                _isLoading.value = false
                signIn(username, password, onResult)
            }

            override fun onError(e: Exception) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(false))
            }
        })
    }

    fun resendConfirmationCode(username: String, onResult: (AWSMobileClientResponse<SignUpResult>) -> Unit) {
        _isSending.value = true
        AWSMobileClient.getInstance().resendSignUp(username, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                _isSending.value = false
                onResult(AWSMobileClientResponse(true, signUpResult))
            }

            override fun onError(e: Exception) {
                _isSending.value = false
                onResult(AWSMobileClientResponse(false))
            }
        })
    }

    fun signIn(username: String, password: String, onResult: (AWSMobileClientResponse<SignInResult>) -> Unit) {
        _isLoading.value = true
        AWSMobileClient.getInstance().signIn(username, password, null, object : Callback<SignInResult> {
            override fun onResult(signInResult: SignInResult) {
                _isLoading.value = false
                if (signInResult.signInState == SignInState.DONE) {
                    userRepository.setLoggedIn(AWSMobileClient.getInstance().tokens, username, AWSMobileClient.getInstance().userSub)
                }

                onResult(AWSMobileClientResponse(true, signInResult))
            }

            override fun onError(e: Exception) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(false, null, e))
            }
        })
    }
}