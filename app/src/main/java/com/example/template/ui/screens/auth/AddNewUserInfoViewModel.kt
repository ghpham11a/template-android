package com.example.template.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.template.models.AWSMobileClientResponse
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddNewUserInfoViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    companion object {
        const val TAG = "AddInfoViewModel"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _firstName = MutableStateFlow("Anthony")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("Pham")
    val lastName: StateFlow<String> = _lastName

    // YYYY-MM-DD

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _dateOfBirth = MutableStateFlow("1988-10-11")
    val dateOfBirth: StateFlow<String> = _dateOfBirth

    private val _username = MutableStateFlow("gm.pham@gmail.com")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("ABcd1234$$")
    val password: StateFlow<String> = _password

    fun onFirstNameChange(value: String) {
        _firstName.value = value
    }

    fun onLastNameChange(value: String) {
        _lastName.value = value
    }

    fun onUsernameChange(value: String) {
        _username.value = value
    }
    fun onDateOfBirthChange(value: String) {
        _dateOfBirth.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun createAccount(
        dateOfBirth: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String,
        phoneNumber: String,
        onResult: (AWSMobileClientResponse<SignUpResult>) -> Unit
    ) {
        _isLoading.value = true
        val userAttributes = HashMap<String, String>()
        userAttributes["birthdate"] = dateOfBirth
        userAttributes["email"] = username
        userAttributes["given_name"] = firstName
        userAttributes["family_name"] = lastName

        if (phoneNumber != "NONE") {
            userAttributes["phone_number"] = phoneNumber
        }

        AWSMobileClient.getInstance().signUp(username, password, userAttributes, null, object : Callback<SignUpResult> {
            override fun onResult(signUpResult: SignUpResult) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(true, signUpResult))
            }

            override fun onError(e: Exception) {
                _isLoading.value = false
                onResult(AWSMobileClientResponse(true, null, e))
            }
        })
    }
}