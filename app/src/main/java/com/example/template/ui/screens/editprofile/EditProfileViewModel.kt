package com.example.template.ui.screens.editprofile

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
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _userSub = MutableStateFlow("")
    val userSub: StateFlow<String> = _userSub

    init {
        userRepository.isLoggedIn()
        _userSub.value = userRepository.userSub ?: ""
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
                val response = Lambdas.api.updateUser(
                    Lambdas.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userRepository.userSub ?: "",
                    UpdateUserBody(
                        updateImage = UpdateImage(encodeImageToBase64(image))
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.contains("Image uploaded successfully!")) {
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
}