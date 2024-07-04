package com.example.template.ui.screens.addpayout

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import com.example.template.models.CreatePayoutMethodRequest
import com.example.template.models.CreatePayoutMethodResponse
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBankInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun handleContinue(country: String, currency: String, accountHolderName: String, routingNumber: String, accountNumber: String): CreatePayoutMethodResponse? {
        _isLoading.value = true
        val response = APIGateway.api.privateCreatePayoutMethod(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userId ?: "",
            CreatePayoutMethodRequest(
                route = "hosted_manual_entry",
                customerId = userRepository.userPrivate?.user?.stripeCustomerId ?: "",
                accountId = userRepository.userPrivate?.user?.stripeAccountId ?: "",
                country = country,
                currency = currency,
                accountHolderName = accountHolderName,
                routingNumber = routingNumber,
                accountNumber = accountNumber
            )
        )
        _isLoading.value = false
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}