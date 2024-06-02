package com.example.template.ui.screens.publicprofile

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
class PublicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isEditable = MutableStateFlow(false)
    val isEditable: StateFlow<Boolean> = _isEditable

    fun checkIfEditable(username: String) {
        viewModelScope.launch {
            _isEditable.value = userRepository.username == username
        }
    }
}