package com.example.template.ui.screens.charlie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharlieViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoggedIn = MutableStateFlow(userRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        userRepository.isLoggedIn()
        viewModelScope.launch {
            userRepository.isAuthenticated.collect { newValue ->
                _isLoggedIn.value = newValue
            }
        }
    }
}