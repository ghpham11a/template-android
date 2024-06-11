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

    fun checkIfEditable(username: String) {
        viewModelScope.launch {
            _isEditable.value = userRepository.username == username
        }
    }
}