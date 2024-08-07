package com.example.template.ui.screens.personalinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails
import com.example.template.models.AWSMobileClientResponse
import com.example.template.models.UpdateEmail
import com.example.template.models.UpdateLegalName
import com.example.template.models.UpdatePhoneNumber
import com.example.template.models.UpdatePreferredName
import com.example.template.models.UpdateUserBody
import com.example.template.networking.APIGateway
import com.example.template.repositories.UserRepository
import com.example.template.utils.Constants
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

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

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
    // save this until user confirms
    var tempEmail: String = ""

    val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading

    init {
        viewModelScope.launch {
            val response = userRepository.privateReadUser()
            if (response != null && response.isSuccessful) {
                // Handle the successful response
                val user = response.body()
                _firstName.value = user?.user?.firstName ?: ""
                _lastName.value = user?.user?.lastName ?: ""
                _preferredName.value = user?.user?.preferredName ?: ""
                _countryCode.value = Constants.COUNTRY_CODES.find { it.contains(user?.user?.countryCode ?: "") }
                    ?: Constants.COUNTRY_CODES.first()
                _phoneNumber.value = (user?.user?.phoneNumber ?: "").replace(user?.user?.countryCode ?: "", "")
                _phoneNumberToDisplay.value =
                    (user?.user?.countryCode ?: "") + " " + (user?.user?.phoneNumber ?: "")
                _email.value = userRepository.username ?: ""
            } else {
                // Handle the error
                println("Error reading user")
            }
            _isScreenLoading.value = false
        }
    }

    suspend fun updateLegalName(firstName: String, lastName: String): Boolean {
        _isLoading.value = true
        return try {
            val result = executeUpdate(UpdateUserBody(updateLegalName = UpdateLegalName(firstName, lastName)))
            if (result) {
                _firstName.value = firstName
                userRepository.updateUser(Constants.UserAttributes.FirstName, firstName)
                _lastName.value = lastName
                userRepository.updateUser(Constants.UserAttributes.LastName, lastName)
            }
            result
        } catch (e: Exception) {
            // Handle exception
            false
        }
    }

    suspend fun updatePreferredName(preferredName: String): Boolean {
        _isLoading.value = true
        return try {
            val result = executeUpdate(UpdateUserBody(updatePreferredName = UpdatePreferredName(preferredName)))
            _isLoading.value = false
            if (result) {
                _preferredName.value = preferredName
                userRepository.updateUser(Constants.UserAttributes.PreferredName, preferredName)
            }
            result
        } catch (e: Exception) {
            // Handle exception
            _isLoading.value = false
            false
        }
    }

    suspend fun updatePhoneNumber(countryCode: String, phoneNumber: String): Boolean {
        _isLoading.value = true
        val code = "+${(countryCode.filter { it.isDigit() })}"
        return try {
            val result = executeUpdate(
                UpdateUserBody(
                    updatePhoneNumber = UpdatePhoneNumber(
                        code,
                        "$code$phoneNumber",
                        userRepository.username ?: ""
                    )
                )
            )
            _isLoading.value = false
            if (result) {
                _countryCode.value = countryCode
                _phoneNumber.value = phoneNumber
                _phoneNumberToDisplay.value = "$code $phoneNumber"
            }
            result
        } catch (e: Exception) {
            // Handle exception
            _isLoading.value = false
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

    suspend fun updateEmail(email: String): Boolean {
        _isLoading.value = true
        return try {
            _email.value = email
            val result = executeUpdate(
                UpdateUserBody(
                    updateEmail = UpdateEmail(email = email)
                )
            )
            _isLoading.value = false
            if (result) {
                tempEmail = email
            }
            result
        } catch (e: Exception) {
            // Handle exception
            _isLoading.value = false
            false
        }
    }

    fun confirmEmailChange(verificationCode: String, onResult: (AWSMobileClientResponse<Void>) -> Unit) {
        _isLoading.value = true
        AWSMobileClient.getInstance().confirmUpdateUserAttribute("email", verificationCode, object : Callback<Void> {
            override fun onResult(result: Void?) {
                userRepository.updateUser(Constants.SHARED_PREFERENCES_KEY_USERNAME, _email.value)
                _email.value = tempEmail
                _isLoading.value = false
                onResult(AWSMobileClientResponse(true, result, null))
            }

            override fun onError(e: Exception) {
                _isLoading.value = false
                // Handle the error
                onResult(AWSMobileClientResponse(false, null, e))
            }
        })
    }

    fun resendConfirmationCode(username: String, onResult: (AWSMobileClientResponse<SignUpResult>) -> Unit) {
        _isSending.value = true
        AWSMobileClient.getInstance().resendSignUp(username, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                _isSending.value = false
                onResult(AWSMobileClientResponse(true, signUpResult))
            }

            override fun onError(e: Exception) {
                _isSending.value = false
                onResult(AWSMobileClientResponse(false))
            }
        })
    }

    private suspend fun executeUpdate(body: UpdateUserBody): Boolean {
        val response = APIGateway.api.privateUpdateUser(
            APIGateway.buildAuthorizedHeaders(userRepository.idToken ?: ""),
            userRepository.userId ?: "",
            body
        )
        return response.isSuccessful
    }
}