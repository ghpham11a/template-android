package com.example.template.ui.screens.alpha

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlphaViewModel: ViewModel() {
    private val _text = MutableLiveData("Hello, World!")
    val text: LiveData<String> = _text

    fun updateText(newText: String) {
        _text.value = newText
    }
}