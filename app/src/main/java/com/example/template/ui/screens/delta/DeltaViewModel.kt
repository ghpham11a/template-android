package com.example.template.ui.screens.delta

import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeltaViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun logOut() {
        AWSMobileClient.getInstance().signOut()
        userRepository.logOut()
    }
}