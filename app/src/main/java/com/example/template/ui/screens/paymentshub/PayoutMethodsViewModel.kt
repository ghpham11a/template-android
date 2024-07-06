package com.example.template.ui.screens.paymentshub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.models.DeletePayoutMethodRequest
import com.example.template.models.DeletePayoutMethodResponse
import com.example.template.models.PayoutMethod
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PayoutMethodsViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _payoutMethods = MutableStateFlow<List<PayoutMethod>>(emptyList())
    val payoutMethods: StateFlow<List<PayoutMethod>> = _payoutMethods

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _accountLinkUrl = MutableStateFlow("")
    val accountLinkUrl: StateFlow<String> = _accountLinkUrl

    var selectedPayoutMethodId = ""

    init {
        viewModelScope.launch {
            fetchPayoutMethods()
        }

        userRepository.userPrivate?.stripeAccount?.accountLinkUrl?.let {
            _accountLinkUrl.value = it
        } ?: run {
            _accountLinkUrl.value = ""
        }
    }

    suspend fun fetchPayoutMethods() {
        val response = APIGateway.api.privateReadPayoutMethods(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userId ?: "",
            userRepository.userPrivate?.user?.stripeAccountId ?: ""
        )
        if (response.isSuccessful) {
            _payoutMethods.value = response.body()?.payoutMethods ?: emptyList()
        }
    }

    suspend fun deletePayoutMethod(payoutMethodId: String): DeletePayoutMethodResponse? {
        _isLoading.value = true

        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.privateDeletePayoutMethod(
                    headers = APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
                    userId = userRepository.userId ?: "",
                    body = DeletePayoutMethodRequest(
                        userRepository.userPrivate?.user?.stripeAccountId ?: "",
                        payoutMethodId
                    )
                )
                _isLoading.value = false
                if (response.isSuccessful) {
                    selectedPayoutMethodId = ""
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
        _isRefreshing.value = true
        val response = userRepository.privateReadUser(refresh = true)
        _isRefreshing.value = false
        if (response != null && response.isSuccessful) {
            val user = response.body()
            userRepository.userPrivate?.stripeAccount?.accountLinkUrl?.let {
                _accountLinkUrl.value = it
            } ?: run {
                _accountLinkUrl.value = ""
            }
        }
    }
}