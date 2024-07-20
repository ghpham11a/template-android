package com.example.template.ui.screens.chathub

import androidx.lifecycle.ViewModel
import com.example.template.models.ChatEvent
import com.example.template.models.VideoCallEvent
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatHubViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _event = MutableStateFlow<List<ChatEvent>>(emptyList())
    val event: StateFlow<List<ChatEvent>> = _event

    suspend fun fetchUsers() {
        val response = APIGateway.api.readUsers(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: "")
        )
        if (response.isSuccessful) {
            _event.value = response.body()?.users?.map { ChatEvent(user = it) } ?: emptyList()
        } else {

        }
    }
}