package com.example.template.ui.screens.bravo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.Todo
import com.example.template.networking.JSONPlaceholder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// @HiltViewModel
class BravoViewModel : ViewModel() {
    private val _data = MutableStateFlow<List<Todo>>(emptyList())
    val data: StateFlow<List<Todo>> = _data

    init {
        fetchData()
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
}