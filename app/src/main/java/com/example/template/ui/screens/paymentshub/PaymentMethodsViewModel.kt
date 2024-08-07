package com.example.template.ui.screens.paymentshub

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.CreateSetupIntentRequest
import com.example.template.models.CreateSetupIntentResponse
import com.example.template.models.DeletePaymentMethodRequest
import com.example.template.models.DeletePaymentMethodResponse
import com.example.template.models.PaymentMethod
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class PaymentMethodsViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var selectedPaymentMethodId = ""

    init {
        viewModelScope.launch {
            fetchPaymentMethods()
        }
    }

    suspend fun fetchPaymentMethods() {
        val response = APIGateway.api.privateReadPaymentMethods(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userId ?: "",
            userRepository.userPrivate?.user?.stripeCustomerId ?: ""
        )
        if (response.isSuccessful) {
            _paymentMethods.value = response.body()?.paymentMethods ?: emptyList()
        }
    }

    suspend fun createSetupIntent(): CreateSetupIntentResponse? {
        _isLoading.value = true

        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.privateCreatePaymentSetupIntent(
                    headers = APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userId = userRepository.userId ?: "",
                    body = CreateSetupIntentRequest(
                        userRepository.userPrivate?.user?.stripeCustomerId ?: ""
                    )
                )
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                _isLoading.value = false
                null
            }
        }
    }

    suspend fun deletePaymentmethod(paymentMethodId: String): DeletePaymentMethodResponse? {
        _isLoading.value = true
        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.privateDeletePaymentMethod(
                    headers = APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userId = userRepository.userId ?: "",
                    body = DeletePaymentMethodRequest(
                        userRepository.userPrivate?.user?.stripeAccountId ?: "",
                        paymentMethodId
                    )
                )
                _isLoading.value = false
                if (response.isSuccessful) {
                    selectedPaymentMethodId = ""
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                _isLoading.value = false
                null
            }
        }
    }

    suspend fun readUser() {
        val response = userRepository.privateReadUser()
        if (response != null && response.isSuccessful) {
            val user = response.body()

        }
    }
}