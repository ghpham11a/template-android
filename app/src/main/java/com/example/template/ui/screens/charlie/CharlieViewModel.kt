package com.example.template.ui.screens.charlie

import androidx.lifecycle.ViewModel
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharlieViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
}