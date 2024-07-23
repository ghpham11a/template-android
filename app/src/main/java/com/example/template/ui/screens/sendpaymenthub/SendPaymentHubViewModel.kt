package com.example.template.ui.screens.sendpaymenthub

import androidx.lifecycle.ViewModel
import com.example.template.models.DynamoDBUser
import com.example.template.models.PaymentReceiver
import com.example.template.networking.APIGateway
import com.example.template.networking.APIGatewayUsers
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SendPaymentHubViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _users = MutableStateFlow<List<DynamoDBUser>>(emptyList())
    val users: StateFlow<List<DynamoDBUser>> = _users

    suspend fun fetchStripeAccounts() {
        val response = APIGatewayUsers.api.readUsers(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: "")
        )
        if (response.isSuccessful) {
            _users.value = response.body()?.users ?: emptyList()
        }
    }
}