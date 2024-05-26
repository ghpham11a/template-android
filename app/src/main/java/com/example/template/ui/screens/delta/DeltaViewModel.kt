package com.example.template.ui.screens.delta

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.amazonaws.mobile.client.results.SignInResult
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeltaViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "DeltaViewModel"
    }

    fun logOut() {

        AWSMobileClient.getInstance().signOut()
//        val signOutOptions = SignOutOptions.builder().signOutGlobally(true).build()
//        AWSMobileClient.getInstance().signOut(signOutOptions, object : Callback<Void> {
//            override fun onResult(result: Void) {
//                Log.d(TAG, "Successfully signed out")
//                userRepository.logOut()
//                // Handle successful sign out here
//            }
//
//            override fun onError(e: Exception) {
//                // Handle sign out error here
//                Log.e(TAG, "Error signing out: ", e)
//            }
//        })
    }
}