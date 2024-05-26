package com.example.template.ui.screens.alpha

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlphaViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val isLoggedIn: LiveData<Boolean> = MutableLiveData(userRepository.isLoggedIn())

    private val _text = MutableLiveData("Hello, World!")
    val text: LiveData<String> = _text

    fun updateText(newText: String) {
        _text.value = newText
    }
}