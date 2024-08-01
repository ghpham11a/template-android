package com.example.template.ui.screens.schedulerhub

import androidx.lifecycle.ViewModel
import com.example.template.models.ChatEvent
import com.example.template.models.CreateChatRequest
import com.example.template.models.CreateVideoCallRequest
import com.example.template.models.DynamoDBUser
import com.example.template.models.VoiceCallEvent
import com.example.template.networking.APIGateway
import com.example.template.networking.APIGatewayEvents
import com.example.template.networking.APIGatewayUsers
import com.example.template.repositories.EventsRepository
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SchedulerViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun fetchUser(userId: String, availabilityType: String) {
        val response = APIGateway.api.publicReadUser(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userId
        )
        if (response.isSuccessful) {
            when (availabilityType) {
                "1" -> {

                }
                "2" -> {

                }
                "3" -> {

                }
                "4" -> {

                }
            }
        } else {

        }
    }
}