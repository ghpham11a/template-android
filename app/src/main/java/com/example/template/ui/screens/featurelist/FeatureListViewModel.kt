package com.example.template.ui.screens.featurelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Todo
import com.example.template.networking.JSONPlaceholder
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeatureListViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _data = MutableStateFlow<List<Todo>>(emptyList())
    val data: StateFlow<List<Todo>> = _data

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: MutableStateFlow<Boolean> = _isLoggedIn

    init {
        userRepository.isLoggedIn()
        viewModelScope.launch {
            userRepository.isAuthenticated.collect { newValue ->
                _isLoggedIn.value = newValue
                if (newValue) {
                    fetchData()
                }
            }
        }
    }

    private fun fetchData() {

        viewModelScope.launch {
            try {
                val response = JSONPlaceholder.api.getTodo()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _data.value = it
                    }
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun logIn() {

    }
}