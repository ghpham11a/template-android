package com.example.template.ui.screens.schedulerhub

import androidx.lifecycle.ViewModel
import com.example.template.models.ChatEvent
import com.example.template.models.CreateChatRequest
import com.example.template.models.CreateVideoCallRequest
import com.example.template.models.DynamoDBUser
import com.example.template.models.ReadUserPublicResponse
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
class SchedulerHubViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _users = MutableStateFlow<List<DynamoDBUser>>(emptyList())
    val users: StateFlow<List<DynamoDBUser>> = _users


    suspend fun fetchUsers(refresh: Boolean = false) {
        val response = APIGatewayUsers.api.readUsers(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: "")
        )
        if (response.isSuccessful) {
            val newUsers = mutableListOf<DynamoDBUser>()
            response.body()?.users?.forEach {
                if (it.userId != userRepository.userId) {
                    newUsers.add(it)
                }
            }
            _users.value = newUsers
        } else {

        }
    }
}