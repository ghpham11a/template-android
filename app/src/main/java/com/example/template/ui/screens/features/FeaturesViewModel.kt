package com.example.template.ui.screens.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Feature
import com.example.template.models.Todo
import com.example.template.networking.JSONPlaceholder
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturesViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _data = MutableStateFlow<List<Todo>>(emptyList())
    val data: StateFlow<List<Todo>> = _data


    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: MutableStateFlow<Boolean> = _isLoggedIn

    private val _newItems = MutableStateFlow<List<Feature>>(emptyList())
    val newItems: MutableStateFlow<List<Feature>> = _newItems

    private val _oldItems = MutableStateFlow<List<Feature>>(emptyList())
    val oldItems: MutableStateFlow<List<Feature>> = _oldItems

    init {
        userRepository.isLoggedIn()
        viewModelScope.launch {
            userRepository.isAuthenticated.collect { newValue ->
                _isLoggedIn.value = newValue
            }
        }
        _newItems.value = listOf(
            Feature("New Item 1", "New Item 1 Description"),
            Feature("New Item 2", "New Item 2 Description"),
            Feature("New Item 3", "New Item 3 Description")
        )

        _oldItems.value = listOf(
            Feature("Old Item 1", "Old Item 1 Description"),
            Feature("Old Item 2", "Old Item 2 Description"),
            Feature("Old Item 3", "Old Item 3 Description")
        )
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