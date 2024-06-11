package com.example.template.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.ForgotPasswordResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.template.models.AWSMobileClientResponse
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "NewPasswordViewModel"
    }

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _newPasswordVerification = MutableStateFlow("")
    val newPasswordVerification: StateFlow<String> = _newPasswordVerification

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onNewPasswordChange(newPassword: String) {
        _newPassword.value = newPassword
    }

    fun onNewPasswordVerificationChange(newPasswordVerification: String) {
        _newPasswordVerification.value = newPasswordVerification
    }

    fun confirmPasswordReset(
        username: String,
        password: String,
        passwordVerification: String,
        confirmationCode: String,
        onResult: (AWSMobileClientResponse<ForgotPasswordResult>) -> Unit
    ) {

        if (password != passwordVerification) {
            return
        }

        AWSMobileClient.getInstance().confirmForgotPassword(username, password, confirmationCode,
            object : Callback<ForgotPasswordResult> {
                override fun onResult(result: ForgotPasswordResult) {
                    onResult(AWSMobileClientResponse(true, null, null))
                }

                override fun onError(e: Exception) {
                    onResult(AWSMobileClientResponse(false, null, e))
                }
            }
        )
    }
}