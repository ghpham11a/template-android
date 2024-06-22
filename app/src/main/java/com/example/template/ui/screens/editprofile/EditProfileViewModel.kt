package com.example.template.ui.screens.editprofile

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.UpdateImage
import com.example.template.models.UpdateSchool
import com.example.template.models.UpdateUserBody
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userSub = MutableStateFlow("")
    val userSub: StateFlow<String> = _userSub

    private val _schoolName = MutableStateFlow("")
    val schoolName: StateFlow<String> = _schoolName

    init {
        userRepository.isLoggedIn()
        _userSub.value = userRepository.userId ?: ""
    }

    fun onSchoolNameChange(schoolName: String) {
        _schoolName.value = schoolName
    }

    fun fetchUser() {
        viewModelScope.launch {
            val response = userRepository.publicReadUser(userRepository.userId ?: "")
            if (response != null && response.isSuccessful) {
                // Handle the successful response
                val user = response.body()
                _schoolName.value = user?.schoolName ?: ""

            } else {
                // Handle the error
                println("Error reading user")
            }
            _isScreenLoading.value = false
        }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    suspend fun updateUser(image: Bitmap): Boolean = suspendCancellableCoroutine { continuation ->

        viewModelScope.launch {
            try {
                val response = APIGateway.api.privateUpdateUser(
                    APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userRepository.userId ?: "",
                    UpdateUserBody(
                        updateImage = UpdateImage(encodeImageToBase64(image))
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message?.contains("Image uploaded successfully!") == true) {
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

            }
        }
    }

    suspend fun updateSchoolName(schoolName: String): Boolean {
        _isLoading.value = true
        return try {
            executeUpdate(UpdateUserBody(updateSchool = UpdateSchool(schoolName)))
        } catch (e: Exception) {
            // Handle exception
            _isLoading.value = false
            false
        }
    }

    private suspend fun executeUpdate(body: UpdateUserBody): Boolean {
        val response = APIGateway.api.privateUpdateUser(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userId ?: "",
            body
        )
        _isLoading.value = false
        return response.isSuccessful
    }
}