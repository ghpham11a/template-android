package com.example.template.ui.screens.xmlview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.networking.JSONPlaceholder
import com.example.template.networking.JSONPlaceholderService
import com.example.template.repositories.UserRepository
import com.example.template.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class XMLViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _textContent = MutableStateFlow("")
    val textContent: StateFlow<String> = _textContent

    suspend fun makeAPICall() {
        viewModelScope.launch {
            val response = JSONPlaceholder.api.getTodo()
            if (response.isSuccessful) {
                response.body()

                _textContent.value = response.body().toString()
            }
        }
    }
}