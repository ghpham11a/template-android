package com.example.template.ui.screens.personalinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignUpResult
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails
import com.example.template.models.AWSMobileClientResponse
import com.example.template.models.UpdateLegalName
import com.example.template.models.UpdatePhoneNumber
import com.example.template.models.UpdatePreferredName
import com.example.template.models.UpdateUserBody
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import com.example.template.utils.Constants.COUNTRY_CODES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Legal name
    val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    val _preferredName = MutableStateFlow("")
    val preferredName: StateFlow<String> = _preferredName

    val _countryCode = MutableStateFlow("")
    val countryCode: StateFlow<String> = _countryCode

    val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    val _phoneNumberToDisplay = MutableStateFlow("")
    val phoneNumberToDisplay: StateFlow<String> = _phoneNumberToDisplay

    val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading

    init {
        viewModelScope.launch {
            val response = userRepository.privateReadUser()
            if (response != null && response.isSuccessful) {
                // Handle the successful response
                val user = response.body()
                _firstName.value = user?.firstName ?: ""
                _lastName.value = user?.lastName ?: ""
                _preferredName.value = user?.preferredName ?: ""
                _countryCode.value = COUNTRY_CODES.find { it.contains(user?.countryCode ?: "") }
                    ?: COUNTRY_CODES.first()
                _phoneNumber.value = (user?.phoneNumber ?: "").replace(user?.countryCode ?: "", "")
                _phoneNumberToDisplay.value =
                    (user?.countryCode ?: "") + " " + (user?.phoneNumber ?: "")
            } else {
                // Handle the error
                println("Error reading user")
            }
            _isScreenLoading.value = false
        }
    }

    suspend fun updatePhoneNumber(countryCode: String, phoneNumber: String): Boolean {
        val code = "+${(countryCode.filter { it.isDigit() })}"
        return try {
            executeUpdate(
                UpdateUserBody(
                    updatePhoneNumber = UpdatePhoneNumber(
                        code,
                        "$code$phoneNumber",
                        userRepository.username ?: ""
                    )
                )
            )
        } catch (e: Exception) {
            // Handle exception
            false
        }
    }

    fun triggerPhoneNumberVerification(onResult: (AWSMobileClientResponse<UserCodeDeliveryDetails>) -> Unit) {
        AWSMobileClient.getInstance().verifyUserAttribute("phone_number", object :
            Callback<UserCodeDeliveryDetails> {
            override fun onResult(signUpResult: UserCodeDeliveryDetails) {
                onResult(AWSMobileClientResponse(true, signUpResult, null))
            }

            override fun onError(e: Exception) {
                onResult(AWSMobileClientResponse(false, null, e))
            }
        })
    }

    suspend fun updateLegalName(firstName: String, lastName: String): Boolean {
        return try {
            executeUpdate(UpdateUserBody(updateLegalName = UpdateLegalName(firstName, lastName)))
        } catch (e: Exception) {
            // Handle exception
            false
        }
    }

    suspend fun updatePreferredName(preferredName: String): Boolean {
        return try {
            executeUpdate(UpdateUserBody(updatePreferredName = UpdatePreferredName(preferredName)))
        } catch (e: Exception) {
            // Handle exception
            false
        }
    }

    private suspend fun executeUpdate(body: UpdateUserBody): Boolean {
        val response = APIGateway.api.updateUser(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userSub ?: "",
            body
        )
        return response.isSuccessful
    }
}