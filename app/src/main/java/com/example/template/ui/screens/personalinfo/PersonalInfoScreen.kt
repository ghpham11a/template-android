package com.example.template.ui.screens.personalinfo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.buttons.TextButton
import com.example.template.ui.components.inputs.ExpandableSection
import com.example.template.ui.components.inputs.PhoneNumberField
import com.example.template.ui.screens.loginandsecurity.LoginAndSecurityViewModel
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonalInfoScreen(navController: NavController) {

    val viewModel = hiltViewModel<PersonalInfoViewModel>()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()

    // Legal name
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    var isLegalNameExpanded by remember { mutableStateOf(false) }
    var isLegalNameEnabled by remember { mutableStateOf(true) }

    var isPreferredFirstNameExpanded by remember { mutableStateOf(false) }
    var isPreferredFirstNameEnabled by remember { mutableStateOf(true) }

    // Phone number
    val countryCode by viewModel.countryCode.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val phoneNumberToDisplay by viewModel.phoneNumberToDisplay.collectAsState()
    var isPhoneNumberExpanded by remember { mutableStateOf(false) }
    var isPhoneNumberEnabled by remember { mutableStateOf(true) }

    var isEmailExpanded by remember { mutableStateOf(false) }
    var isEmailEnabled by remember { mutableStateOf(true) }

    var isAddressExpanded by remember { mutableStateOf(false) }
    var isAddressEnabled by remember { mutableStateOf(true) }

    var isEmergencyContactExpanded by remember { mutableStateOf(false) }
    var isEmergencyContactEnabled by remember { mutableStateOf(true) }

    fun updateEnabledAndDisabledSections(field: String, isEnabled: Boolean) {
        when (field) {
            "Legal Name" -> {
                isPreferredFirstNameEnabled = !isEnabled
                isPhoneNumberEnabled = !isEnabled
                isEmailEnabled = !isEnabled
                isAddressEnabled = !isEnabled
                isEmergencyContactEnabled = !isEnabled
            }
            "Preferred first name" -> {
                isLegalNameEnabled = !isEnabled
                isPhoneNumberEnabled = !isEnabled
                isEmailEnabled = !isEnabled
                isAddressEnabled = !isEnabled
                isEmergencyContactEnabled = !isEnabled
            }
            "Phone number" -> {
                isLegalNameEnabled = !isEnabled
                isPreferredFirstNameEnabled = !isEnabled
                isEmailEnabled = !isEnabled
                isAddressEnabled = !isEnabled
                isEmergencyContactEnabled = !isEnabled
            }
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TextButton(
                onClick = {
                    navController.navigateUp()
                },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState, true)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            ExpandableSection(
                title = "Legal Name",
                isExpanded = isLegalNameExpanded,
                onExpandedChange = {
                    updateEnabledAndDisabledSections("Legal Name", it)
                    isLegalNameExpanded = it
                },
                isEnabled = isLegalNameEnabled,
                closedContent = {
                    if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                        Text("$firstName $lastName")
                    } else {
                        Text("Update your legal name")
                    }
                },
                openedContent = {
                    var firstNameField by remember { mutableStateOf(TextFieldValue(viewModel.firstName.value)) }
                    var lastNameField by remember { mutableStateOf(TextFieldValue(viewModel.lastName.value)) }
                    Column {
                        Text("Your calendar may be blocked for up to an hour while we verify your new legal name.")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = firstNameField,
                            onValueChange = { firstNameField = it },
                            label = { Text("First name on ID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = lastNameField,
                            onValueChange = { lastNameField = it },
                            label = { Text("Last name on ID") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LoadingButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    if (viewModel.updateLegalName(firstNameField.text, lastNameField.text)) {
                                        isLegalNameExpanded = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = isLoading,
                            buttonText = "Save"
                        )
                    }
                }
            )

            ExpandableSection(
                title = "Preferred first name",
                isExpanded = isPreferredFirstNameExpanded,
                onExpandedChange = {
                    updateEnabledAndDisabledSections("Preferred first name", it)
                    isPreferredFirstNameExpanded = it
                                   },
                isEnabled = isPreferredFirstNameEnabled,
                closedContent = {
                    Text("FirstName")
                },
                openedContent = {
                    var preferredName by remember { mutableStateOf(TextFieldValue()) }
                    Column {
                        Text("The first name or business name that you'd like to be known by.")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = preferredName,
                            onValueChange = { preferredName = it },
                            label = { Text("Preferred first name (optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LoadingButton(
                            onClick = {

                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = isLoading,
                            buttonText = "Save"
                        )
                    }
                }
            )

            ExpandableSection(
                title = "Phone number",
                isExpanded = isPhoneNumberExpanded,
                onExpandedChange = {
                    updateEnabledAndDisabledSections("Phone number", it)
                    isPhoneNumberExpanded = it
                                   },
                isEnabled = isPhoneNumberEnabled,
                closedContent = {
                    if (phoneNumberToDisplay.isNotEmpty()) {
                        Text(phoneNumberToDisplay)
                    } else {
                        Text("Add a phone number just in case we need to reach you. No one else will ever see it.")
                    }
                },
                openedContent = {

                    var countryCodeField by remember { mutableStateOf(viewModel.countryCode.value) }
                    var phoneNumberField by remember { mutableStateOf(viewModel.phoneNumber.value) }

                    Column {
                        PhoneNumberField(
                            selectedCountryCode = countryCodeField,
                            onCountryCodeChange = { countryCodeField = it },
                            phoneNumber = phoneNumberField,
                            onPhoneNumberChange = { phoneNumberField = it },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LoadingButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    if (viewModel.updatePhoneNumber(countryCodeField, phoneNumberField)) {
                                        isPhoneNumberExpanded = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = isLoading,
                            buttonText = "Save"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}