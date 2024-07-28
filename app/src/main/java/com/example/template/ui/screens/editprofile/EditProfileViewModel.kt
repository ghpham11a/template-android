package com.example.template.ui.screens.editprofile

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.R
import com.example.template.models.Tag
import com.example.template.models.UpdateImage
import com.example.template.models.UpdateSchool
import com.example.template.models.UpdateTags
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

    private val _userTags = MutableStateFlow(emptyList<Tag>())
    val userTags: StateFlow<List<Tag>> = _userTags

    private val _selectedTags = MutableStateFlow(emptyList<Tag>())
    val selectedTags: StateFlow<List<Tag>> = _selectedTags

    init {
        userRepository.isLoggedIn()
        _userSub.value = userRepository.userId ?: ""
    }

    fun onSchoolNameChange(schoolName: String) {
        _schoolName.value = schoolName
    }

    fun fetchUser(context: Context) {
        viewModelScope.launch {
            val response = userRepository.publicReadUser(userRepository.userId ?: "")
            if (response != null && response.isSuccessful) {
                // Handle the successful response
                val user = response.body()
                _schoolName.value = user?.schoolName ?: ""
                _userTags.value = user?.tags?.map { Tag(id = it, title = context.getString(tagTitleFromId(it ?: -1))) } ?: emptyList()
                _selectedTags.value = _userTags.value

            } else {
                // Handle the error
                println("Error reading user")
            }
            _isScreenLoading.value = false
        }
    }
    private fun tagTitleFromId(id: Int): Int {
        return when (id) {
            1 -> R.string.tag_alpha
            2 ->  R.string.tag_bravo
            3 ->  R.string.tag_charlie
            4 ->  R.string.tag_delta
            5 -> R.string.tag_echo
            else ->  -1
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

    fun selectOrDeselectTag(tag: Tag) {
        val newSelectedTags = if (_selectedTags.value.contains(tag)) {
            _selectedTags.value.filter { it.id != tag.id }
        } else {
            _selectedTags.value + tag
        }
        _selectedTags.value = newSelectedTags
    }

    suspend fun updateTags(newTags: List<Tag>): Boolean {
        _isLoading.value = true
        return try {
            if (executeUpdate(UpdateUserBody(updateTags = UpdateTags(newTags.map { it.id ?: -1 })))) {
                _userTags.value = newTags
                true
            } else {
                false
            }
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