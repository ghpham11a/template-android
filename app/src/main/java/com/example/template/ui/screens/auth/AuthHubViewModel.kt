package com.example.template.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails
import com.example.template.models.AWSMobileClientResponse
import com.example.template.models.CheckIfUserExistsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.template.networking.APIGateway
import com.example.template.utils.Constants.AWS_COGNITO_USER_DOES_EXIST_MESSAGE
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthHubViewModel: ViewModel() {

    companion object {
        const val TAG = "AuthViewModel"
    }

    private val _username = MutableStateFlow("gm.pham@gmail.com")
    val username: StateFlow<String> = _username

    private val _selectedCountryCode = MutableStateFlow("United States ( +1 )")
    val selectedCountryCode: StateFlow<String> = _selectedCountryCode

    private val _phoneNumber = MutableStateFlow("9727401265")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isPhoneNumberMode = MutableStateFlow(false)
    val isPhoneNumberMode: StateFlow<Boolean> = _isPhoneNumberMode

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    fun triggerPhoneNumberVerification(onResult: (AWSMobileClientResponse<UserCodeDeliveryDetails>) -> Unit) {

//        AWSMobileClient.getInstance().verifyUserAttribute("phone_number", object :
//            Callback<UserCodeDeliveryDetails> {
//            override fun onResult(signUpResult: UserCodeDeliveryDetails) {
//                onResult(AWSMobileClientResponse(true, signUpResult, null))
//            }
//
//            override fun onError(e: Exception) {
//                onResult(AWSMobileClientResponse(false, null, e))
//            }
//        })
    }

    suspend fun checkIfUserExists(
        username: String = "",
        phoneNumber: String = ""
    ): Boolean = suspendCancellableCoroutine { continuation ->

        _isLoading.value = true

        viewModelScope.launch {

            try {
                var response: Response<CheckIfUserExistsResponse>? = null

                if (username.isNotBlank()) {
                    response = APIGateway.api.adminReadUser(username = username.lowercase())
                }
                if (phoneNumber.isNotBlank()) {
                    response = APIGateway.api.adminReadUser(phoneNumber = phoneNumber)
                }

                if (response?.isSuccessful == true) {
                    response.body()?.let {
                        if (it.message == AWS_COGNITO_USER_DOES_EXIST_MESSAGE) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    }
                } else {
                    continuation.resume(false)
                }
            } catch (e: Exception) {
                // Handle exception
                continuation.resumeWithException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onPhoneNumberModeChange(mode: Boolean) {
        _isPhoneNumberMode.value = mode
    }

    fun onCountryCodeChange(code: String) {
        _selectedCountryCode.value = code
    }

    fun onPhoneNumberChange(number: String) {
        _phoneNumber.value = number
    }
}