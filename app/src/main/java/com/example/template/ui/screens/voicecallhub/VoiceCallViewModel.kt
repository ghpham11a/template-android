package com.example.template.ui.screens.voicecallhub

import androidx.lifecycle.ViewModel
import com.azure.android.communication.calling.Call
import com.azure.android.communication.calling.CallAgent
import com.azure.android.communication.calling.DeviceManager
import com.example.template.repositories.EventsRepository
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoiceCallViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventsRepository: EventsRepository
): ViewModel() {

    var callAgent: CallAgent? = null
    var call: Call? = null
    var deviceManager: DeviceManager? = null

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

    fun getIdentity(id: String): String? {
        eventsRepository.videoCalls?.find { it.id == id }?.let {videoCall ->
            if (videoCall.senderId == userRepository.userId) {
                videoCall.senderIdentity?.let { token ->
                    return token
                }
            } else {
                videoCall.receiverId?.let { token ->
                    return token
                }
            }
        }
        return null
    }
}