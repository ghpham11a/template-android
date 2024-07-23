package com.example.template.ui.screens.proxycallhub

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.template.models.CreateProxyCallRequest
import com.example.template.models.DynamoDBUser
import com.example.template.models.ProxyCallEvent
import com.example.template.networking.APIGateway
import com.example.template.networking.APIGatewayEvents
import com.example.template.networking.APIGatewayUsers
import com.example.template.repositories.EventsRepository
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.reflect.Proxy
import javax.inject.Inject

@HiltViewModel
class ProxyCallHubViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventsRepository: EventsRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _events = MutableStateFlow<List<ProxyCallEvent>>(emptyList())
    val events: StateFlow<List<ProxyCallEvent>> = _events

    fun getPhoneNumber(event: ProxyCallEvent): String {
        if (event.proxyCall?.senderId == userRepository.userId) {
            return event.proxyCall?.receiverProxy ?: ""
        }
        if (event.proxyCall?.receiverId == userRepository.userId) {
            return event.proxyCall?.senderProxy ?: ""
        }
        return ""
    }

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
            _events.value = users.map { ProxyCallEvent(user = it) }
            fetchProxyCalls(refresh)
        } else {

        }
    }

    suspend fun fetchProxyCalls(refresh: Boolean = false) {
        val response = eventsRepository.fetchProxyCalls(refresh)
        response?.let { proxyCalls ->
            val newEvents = mutableListOf<ProxyCallEvent>()
            for (event in _events.value) {
                (proxyCalls.find { it.receiverId == event.user?.userId || it.senderId == event.user?.userId  })?.let {
                    newEvents.add(event.copy(proxyCall = it))
                } ?: run {
                    newEvents.add(event.copy())
                }
            }
            _events.value = newEvents
        }
    }

    suspend fun createProxyCall(receiverId: String) {
        val response = APIGatewayEvents.api.createProxyCall(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            CreateProxyCallRequest(senderId = userRepository.userId, receiverId = receiverId)
        )
        if (response.isSuccessful) {
            fetchUsers(true)
        } else {

        }
    }
}