package com.example.template.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "DeltaViewModel"

        object Cells {
            const val GUEST_BANNER = "GUEST_BANNER"
            const val LOG_IN = "LOGIN"

            const val VIEW_PROFILE = "VIEW_PROFILE"
            const val SETTINGS_HEADING = "SETTINGS_HEADING"
            const val PERSONAL_INFORMATION = "PERSONAL_INFORMATION"
            const val LOGIN_AND_SECURITY = "LOGIN_AND_SECURITY"
            const val PAYMENTS_AND_PAYOUTS = "PAYMENTS_AND_PAYOUTS"
            const val LOG_OUT = "LOG_OUT"
        }
    }

    private val _isLoggedIn = MutableStateFlow(userRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userSub = MutableStateFlow("")
    val userSub: StateFlow<String> = _userSub

    private val _username = MutableStateFlow("")

    var username = ""

    private val _cells = MutableStateFlow(emptyList<String>())
    val cells: StateFlow<List<String>> = _cells


    init {
        userRepository.isLoggedIn()
        viewModelScope.launch {
            userRepository.isAuthenticated.collect { newValue ->
                _isLoggedIn.value = newValue

                _userSub.value = userRepository.userSub ?: ""
                username = userRepository.username ?: ""

                if (newValue) {
                    _cells.value = listOf(
                        Cells.VIEW_PROFILE,
                        Cells.SETTINGS_HEADING,
                        Cells.PERSONAL_INFORMATION,
                        Cells.LOGIN_AND_SECURITY,
                        Cells.PAYMENTS_AND_PAYOUTS,
                        Cells.LOG_OUT
                    )
                } else {
                    _cells.value = listOf(
                        Cells.GUEST_BANNER,
                        Cells.LOG_IN
                    )
                }
            }
        }
    }

    fun logOut() {
        AWSMobileClient.getInstance().signOut()
        userRepository.logOut()
    }
}