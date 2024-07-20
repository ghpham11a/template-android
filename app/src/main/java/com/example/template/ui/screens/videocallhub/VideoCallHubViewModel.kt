package com.example.template.ui.screens.videocallhub

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.template.models.DynamoDBUser
import com.example.template.models.VideoCallEvent
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoCallHubViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _event = MutableStateFlow<List<VideoCallEvent>>(emptyList())
    val event: StateFlow<List<VideoCallEvent>> = _event

    suspend fun fetchUsers() {
        val response = APIGateway.api.readUsers(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: "")
        )
        if (response.isSuccessful) {
            _event.value = response.body()?.users?.map { VideoCallEvent(user = it) } ?: emptyList()
        } else {

        }
    }

    suspend fun fetchAccessToken() {
        val response = userRepository.getAZCSAccessToken(true)
        if (response?.isSuccessful == true) {
            var createAZCSAccessTokenResponse = response.body()
            Log.d("fetchAccessToken", "${createAZCSAccessTokenResponse?.token}")
        } else {
            Log.d("fetchAccessToken", "fail bitch")
        }
    }
}