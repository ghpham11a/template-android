package com.example.template.ui.screens.addpayout

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import com.example.template.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPayoutViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _selectedCountry = MutableStateFlow("United States")
    val selectedCountry: StateFlow<String> = _selectedCountry

    val _availablePayoutTypes = MutableStateFlow<List<String>>(emptyList())
    val availablePayoutTypes: StateFlow<List<String>> = _availablePayoutTypes

    val _selectedPayoutType = MutableStateFlow<String>("")
    val selectedPayoutType: StateFlow<String> = _selectedPayoutType

    fun onCountryChange(country: String) {
        _selectedCountry.value = country

        when (country) {
            "United States" -> {
                _availablePayoutTypes.value = listOf(
                    "Bank Account",
                    "PayPal",
                    "Venmo",
                    "Cash App",
                    "Zelle",
                    "Check"
                )
            }
            else -> {
                _availablePayoutTypes.value = listOf(
                    "Bank Account",
                    "PayPal",
                    "Venmo",
                    "Cash App",
                    "Zelle",
                    "Check",
                    "International Wire"
                )
            }
        }
    }

    fun onPayoutTypeChange(country: String) {
        _selectedPayoutType.value = country
    }
}