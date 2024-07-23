package com.example.template.ui.screens.videocallhub

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.template.models.DynamoDBUser
import com.example.template.models.VideoCallEvent
import com.example.template.networking.APIGateway
import com.example.template.repositories.EventsRepository
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoCallViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventsRepository: EventsRepository
): ViewModel() {

    fun getAccessToken(id: String): String? {
        eventsRepository.videoCalls?.find { it.id == id }?.let {videoCall ->
            if (videoCall.senderId == userRepository.userId) {
                videoCall.senderToken?.let { token ->
                    return token
                }
            } else {
                videoCall.receiverToken?.let { token ->
                    return token
                }
            }
        }
        return null
    }
}