package com.example.template.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.template.networking.Lambdas
import com.example.template.utils.Constants.AWS_COGNITO_USER_DOES_EXIST_MESSAGE
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthHubViewModel: ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _selectedCountryCode = MutableStateFlow("")
    val selectedCountryCode: StateFlow<String> = _selectedCountryCode

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isPhoneNumberMode = MutableStateFlow(false)
    val isPhoneNumberMode: StateFlow<Boolean> = _isPhoneNumberMode

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    suspend fun checkIfUserExists(username: String): Boolean = suspendCancellableCoroutine { continuation ->
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = Lambdas.api.adminGetUser(username.lowercase())
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

    fun onPhoneNumberModeChange(mode: Boolean) {
        _isPhoneNumberMode.value = mode
    }

    fun onCountryCodeChange(code: String) {
        _selectedCountryCode.value = code
    }

    fun onPhoneNumberChange(number: String) {
        _phoneNumber.value = number
    }
}