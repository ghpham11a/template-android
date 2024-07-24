package com.example.template.ui.screens.videocallhub

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.template.models.CreateProxyCallRequest
import com.example.template.models.CreateVideoCallRequest
import com.example.template.models.DynamoDBUser
import com.example.template.models.ProxyCallEvent
import com.example.template.models.VideoCallEvent
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
class VideoCallHubViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventsRepository: EventsRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _events = MutableStateFlow<List<VideoCallEvent>>(emptyList())
    val event: StateFlow<List<VideoCallEvent>> = _events



    suspend fun fetchUsers(refresh: Boolean = false) {
        val response = APIGatewayUsers.api.readUsers(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: "")
        )
        if (response.isSuccessful) {
            val users = mutableListOf<DynamoDBUser>()
            response.body()?.users?.forEach {
                if (it.userId != userRepository.userId) {
                    users.add(it)
                }
            }
            _events.value = users.map { VideoCallEvent(user = it) }
            fetchVideoCalls(refresh)
        } else {

        }
    }

    suspend fun fetchVideoCalls(refresh: Boolean = false) {
        val response = eventsRepository.fetchVideoCalls(refresh)
        response?.let { videoCalls ->
            val newEvents = mutableListOf<VideoCallEvent>()
            for (event in _events.value) {
                (videoCalls.find { it.receiverId == event.user?.userId || it.senderId == event.user?.userId  })?.let {
                    newEvents.add(event.copy(videoCall = it))
                } ?: run {
                    newEvents.add(event.copy())
                }
            }
            _events.value = newEvents
        }
    }

    suspend fun createVideoCall(receiverId: String) {
        val response = APIGatewayEvents.api.createVideoCall(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            CreateVideoCallRequest(senderId = userRepository.userId, receiverId = receiverId)
        )
        if (response.isSuccessful) {
            fetchUsers(true)
        } else {
        }
    }

    suspend fun deleteVideoCall(videoCallId: String) {
        val response = APIGatewayEvents.api.deleteVideoCall(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            videoCallId
        )
        if (response.isSuccessful) {
            fetchUsers(true)
        } else {
        }
    }
}