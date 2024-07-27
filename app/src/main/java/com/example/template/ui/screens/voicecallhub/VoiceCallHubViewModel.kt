package com.example.template.ui.screens.voicecallhub

import androidx.lifecycle.ViewModel
import com.example.template.models.CreateVideoCallRequest
import com.example.template.models.CreateVoiceCallRequest
import com.example.template.models.DynamoDBUser
import com.example.template.models.VideoCallEvent
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
class VoiceCallHubViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventsRepository: EventsRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _events = MutableStateFlow<List<VoiceCallEvent>>(emptyList())
    val event: StateFlow<List<VoiceCallEvent>> = _events

    private val _isLoadingStates = MutableStateFlow(emptyList<Boolean>())
    val isLoadingStates: StateFlow<List<Boolean>> = _isLoadingStates

    suspend fun fetchUsers(refresh: Boolean = false) {
        val response = APIGatewayUsers.api.readUsers(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: "")
        )
        if (response.isSuccessful) {
            val users = mutableListOf<DynamoDBUser>()
            val states = mutableListOf<Boolean>()
            response.body()?.users?.forEach {
                if (it.userId != userRepository.userId) {
                    users.add(it)
                    states.add(false)
                }
            }
            _events.value = users.map { VoiceCallEvent(user = it) }
            _isLoadingStates.value = states
            fetchVoiceCalls(refresh)
        } else {

        }
    }

    suspend fun fetchVoiceCalls(refresh: Boolean = false) {
        val response = eventsRepository.fetchVoiceCalls(refresh)
        response?.let { videoCalls ->
            val newEvents = mutableListOf<VoiceCallEvent>()
            for (event in _events.value) {
                (videoCalls.find { it.receiverId == event.user?.userId || it.senderId == event.user?.userId  })?.let {
                    newEvents.add(event.copy(voiceCall = it))
                } ?: run {
                    newEvents.add(event.copy())
                }
            }
            _events.value = newEvents
        }
    }

    suspend fun createVoiceCall(receiverId: String) {

        val index = _events.value.indexOfFirst { it.user?.userId == receiverId }
        toggleLoadingState(index)

        val response = APIGatewayEvents.api.createVoiceCall(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            CreateVoiceCallRequest(senderId = userRepository.userId, receiverId = receiverId)
        )

        toggleLoadingState(index)

        if (response.isSuccessful) {
            fetchUsers(true)
        } else {
        }
    }

    suspend fun deleteVoiceCall(voiceCallId: String) {

        val index = _events.value.indexOfFirst { it.voiceCall?.id == voiceCallId }
        toggleLoadingState(index)

        val response = APIGatewayEvents.api.deleteVoiceCall(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            voiceCallId
        )

        toggleLoadingState(index)

        if (response.isSuccessful) {
            fetchUsers(true)
        } else {
        }
    }

    private fun toggleLoadingState(index: Int) {
        var states = _isLoadingStates.value.toMutableList()
        states[index] = states[index].not()
        _isLoadingStates.value = states
    }
}