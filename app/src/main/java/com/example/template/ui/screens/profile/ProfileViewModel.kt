package com.example.template.ui.screens.profile

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.example.template.models.AdminUpdateUserBody
import com.example.template.models.UpdateImage
import com.example.template.models.UpdateUserBody
import com.example.template.networking.Lambdas
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "DeltaViewModel"
    }

    private val _isLoggedIn = MutableStateFlow(userRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _disableIsLoading = MutableStateFlow(false)
    val disableIsLoading: StateFlow<Boolean> = _disableIsLoading

    private val _deleteIsLoading = MutableStateFlow(false)
    val deleteIsLoading: StateFlow<Boolean> = _deleteIsLoading

    private val _userSub = MutableStateFlow("")
    val userSub: StateFlow<String> = _userSub

    private val _username = MutableStateFlow("")

    var username = ""


    init {
        userRepository.isLoggedIn()
        viewModelScope.launch {
            userRepository.isAuthenticated.collect { newValue ->
                _isLoggedIn.value = newValue
            }
        }
        username = userRepository.username ?: ""
        _userSub.value = userRepository.userSub ?: ""
    }

    fun logOut() {
        AWSMobileClient.getInstance().signOut()
        userRepository.logOut()
    }

    suspend fun disableUser(): Boolean = suspendCancellableCoroutine { continuation ->
        _disableIsLoading.value = true

        viewModelScope.launch {
            try {
                val response = Lambdas.api.adminUpdateUser(
                    Lambdas.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userRepository.username ?: "",
                    AdminUpdateUserBody("disable", userRepository.username)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.contains("disabled successfully")) {
                            continuation.resume(true)
                            userRepository.logOut()
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
                val response = Lambdas.api.adminDeleteUser(
                    Lambdas.buildAuthorizedHeaders(userRepository.idToken ?: ""),
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