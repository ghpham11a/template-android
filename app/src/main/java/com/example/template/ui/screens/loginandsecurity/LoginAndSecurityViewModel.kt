package com.example.template.ui.screens.loginandsecurity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.example.template.models.AWSMobileClientResponse
import com.example.template.models.AdminUpdateUserBody
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class LoginAndSecurityViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _disableIsLoading = MutableStateFlow(false)
    val disableIsLoading: StateFlow<Boolean> = _disableIsLoading

    private val _deleteIsLoading = MutableStateFlow(false)
    val deleteIsLoading: StateFlow<Boolean> = _deleteIsLoading

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _newPasswordVerification = MutableStateFlow("")
    val newPasswordVerification: StateFlow<String> = _newPasswordVerification

    fun onCurrentPasswordChange(currentPassword: String) {
        _currentPassword.value = currentPassword
    }

    fun onNewPasswordChange(newPassword: String) {
        _newPassword.value = newPassword
    }

    fun onNewPasswordVerificationChange(newPasswordVerification: String) {
        _newPasswordVerification.value = newPasswordVerification
    }

    fun changePassword(currentPassword: String, newPassword: String, onResult: (AWSMobileClientResponse<String?>) -> Unit) {

        _isLoading.value = true

        AWSMobileClient.getInstance().changePassword(currentPassword, newPassword, object : Callback<Void> {
            override fun onResult(result: Void?) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(true, null, null))
            }

            override fun onError(e: Exception) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(false, null, null))
            }
        })
    }

    suspend fun disableUser(): Boolean = suspendCancellableCoroutine { continuation ->
        _disableIsLoading.value = true

        viewModelScope.launch {
            try {
                val response = APIGateway.api.adminDisableUser(
                    APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userRepository.username ?: "",
                    AdminUpdateUserBody("disable", userRepository.username)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.contains("disabled successfully")) {
                            userRepository.logOut()
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
                _disableIsLoading.value = false
            }
        }
    }

    suspend fun deleteUser(): Boolean = suspendCancellableCoroutine { continuation ->
        _deleteIsLoading.value = true

        val username = userRepository.username ?: run {
            continuation.resume(false)
            return@suspendCancellableCoroutine
        }

        viewModelScope.launch {
            try {
                val response = APIGateway.api.adminDeleteUser(
                    APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    username
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.contains("deleted successfully")) {
                            userRepository.logOut()
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
                _deleteIsLoading.value = false
            }
        }
    }
}