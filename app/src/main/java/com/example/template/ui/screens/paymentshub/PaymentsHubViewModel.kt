package com.example.template.ui.screens.paymentshub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.PaymentMethod
import com.example.template.models.PayoutMethod
import com.example.template.models.Todo
import com.example.template.networking.APIGateway
import com.example.template.networking.JSONPlaceholder
import com.example.template.repositories.UserRepository
import com.example.template.ui.screens.profile.ProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsHubViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            val response = userRepository.privateReadUser()
            if (response != null && response.isSuccessful) {

            }
        }
    }
}