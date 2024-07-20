package com.example.template.ui.screens.sendpaymenthub

import androidx.lifecycle.ViewModel
import com.example.template.models.CreatePaymentIntentRequest
import com.example.template.models.PaymentReceiver
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentAmountViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _receivers = MutableStateFlow<List<PaymentReceiver>>(emptyList())
    val receivers: StateFlow<List<PaymentReceiver>> = _receivers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun sendPayment(accountId: String, amount: String): Boolean {

        _isLoading.value = true

        val response = APIGateway.api.stripeCreatePaymentIntent(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            CreatePaymentIntentRequest(
                customerId = userRepository.userPrivate?.user?.stripeCustomerId ?: "",
                accountId = accountId,
                amount = amount
            )
        )

        _isLoading.value = false

        return response.isSuccessful
    }
}