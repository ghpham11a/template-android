package com.example.template.ui.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.SignUpResult
import com.example.template.models.AWSMobileClientResponse
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CodeVerificationViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "CodeVerificationViewModel"
    }

    fun confirmSignUp(username: String, password: String, confirmationCode: String, onResult: (AWSMobileClientResponse<SignInResult>) -> Unit) {
        AWSMobileClient.getInstance().confirmSignUp(username, confirmationCode, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                signIn(username, password, onResult)
            }

            override fun onError(e: Exception) {
                onResult(AWSMobileClientResponse(false))
            }
        })
    }

    fun resendConfirmationCode(username: String, onResult: (AWSMobileClientResponse<SignUpResult>) -> Unit) {
        AWSMobileClient.getInstance().resendSignUp(username, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                onResult(AWSMobileClientResponse(true, signUpResult))
            }

            override fun onError(e: Exception) {
                onResult(AWSMobileClientResponse(false))
            }
        })
    }

    fun signIn(username: String, password: String, onResult: (AWSMobileClientResponse<SignInResult>) -> Unit) {
        AWSMobileClient.getInstance().signIn(username, password, null, object : Callback<SignInResult> {
            override fun onResult(signInResult: SignInResult) {

                if (signInResult.signInState == SignInState.DONE) {
                    userRepository.setLoggedIn(AWSMobileClient.getInstance().tokens, username)
                }

                onResult(AWSMobileClientResponse(true, signInResult))
            }

            override fun onError(e: Exception) {
                onResult(AWSMobileClientResponse(false, null, e))
            }
        })
    }
}