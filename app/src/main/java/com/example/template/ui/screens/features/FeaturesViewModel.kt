package com.example.template.ui.screens.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.Screen
import com.example.template.models.Feature
import com.example.template.models.Todo
import com.example.template.networking.JSONPlaceholder
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturesViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _data = MutableStateFlow<List<Todo>>(emptyList())
    val data: StateFlow<List<Todo>> = _data


    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: MutableStateFlow<Boolean> = _isLoggedIn

    private val _newItems = MutableStateFlow<List<Feature>>(emptyList())
    val newItems: MutableStateFlow<List<Feature>> = _newItems

    private val _oldItems = MutableStateFlow<List<Feature>>(emptyList())
    val oldItems: MutableStateFlow<List<Feature>> = _oldItems

    init {
        userRepository.isLoggedIn()
        viewModelScope.launch {
            userRepository.isAuthenticated.collect { newValue ->
                _isLoggedIn.value = newValue
            }
        }
        _newItems.value = listOf(
            Feature("Thing Builder", "This is a flow that guides you through several steps one at a time", Screen.ThingIntro.route),
            Feature("Thing Updater", "Here you can go into thing details and update the values.", Screen.ThingList.route),
            Feature("Filter List", "List of a lot of items that can be filtered", Screen.FilterList.route),
            Feature("XML View", "This screen was constructed using the prevous XML based view system.", Screen.XMLView.route),
            Feature("Map", "This is a location search screen with a Google map", Screen.Map.route),
            Feature("Tabbed List", "Switch between multiple lists using a tab bar at the top", Screen.TabbedList.route),
            Feature("Send Payment", "Send a payment to another account via Stripe", Screen.SendPaymentHub.route),
            Feature("Masked Phone Call", "Call another number while masking your phone number using Twilio", Screen.ProxyCallHub.route),
            Feature("Video calls", "Make a video call using Azure Communication Service", Screen.VideoCallHub.route),
            Feature("Voice calls", "Make a voice call using Azure Communication Service", Screen.VoiceCallHub.route),
            Feature("Chat", "Chat using Azure Communication Service", Screen.ChatHub.route),
            Feature("Scheduler Hub", "Choose an available time on another user's schedule", Screen.SchedulerHub.route),
        )

        _oldItems.value = listOf(

        )
    }

    private fun fetchData() {

        viewModelScope.launch {
            try {
                val response = JSONPlaceholder.api.getTodo()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _data.value = it
                    }
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun logIn() {

    }
}