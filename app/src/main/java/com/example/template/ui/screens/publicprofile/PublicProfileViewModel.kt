package com.example.template.ui.screens.publicprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isEditable = MutableStateFlow(false)
    val isEditable: StateFlow<Boolean> = _isEditable

    val _schoolName = MutableStateFlow("")
    val schoolName: StateFlow<String> = _schoolName

    val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading

    fun checkIfEditable(userId: String) {
        viewModelScope.launch {
            _isEditable.value = userRepository.userId == userId
        }
    }

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            val response = userRepository.publicReadUser(userId)
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


}