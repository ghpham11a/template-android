package com.example.template.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.inputs.DatePicker
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoScreen(navController: NavController, username: String, phoneNumber: String) {

    val viewModel = hiltViewModel<AddNewUserInfoViewModel>()

    val isLoading by viewModel.isLoading.collectAsState()

    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val dateOfBirth by viewModel.dateOfBirth.collectAsState()
    val email by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val dateOfBirthInteractionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    // viewModel.onUsernameChange(if (username != "NONE") username else "")

    Scaffold(
        topBar = {
            TextButton(
                onClick = {
                    navController.navigateUp()
                },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Add your info", style = MaterialTheme.typography.headlineMedium)

            Text(text = "Legal name", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = firstName,
                onValueChange = { viewModel.onFirstNameChange(it) },
                label = { Text("First name on ID") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { viewModel.onLastNameChange(it) },
                label = { Text("Last name on ID") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Date of birth", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = {  },
                label = { Text("Birthday") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = dateOfBirthInteractionSource,
                        indication = null,
                        onClick = {
                            showDialog = true
                            focusManager.clearFocus()
                        }
                    ),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (phoneNumber != Constants.PATH_PLACEHOLDER) {
                Text(text = "Phone Number", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {  },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(text = "Email", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = phoneNumber != Constants.PATH_PLACEHOLDER,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Password", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                isLoading = isLoading,
                onClick = {
                    viewModel.createAccount(dateOfBirth, email, firstName, lastName, password, phoneNumber) { response ->
                        coroutineScope.launch(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                navController.navigate(Screen.CodeVerification.build("SIGN_UP", email, password))
                            } else {
                                navController.popBackStack(Screen.AuthHub.route, true)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Sign up"
            )

            if (showDialog) {
                DatePicker(
                    onDismissRequest = { showDialog = false },
                    onDateSelected = { date ->
                        viewModel.onDateOfBirthChange(date)
                        showDialog = false
                    }
                )
            }
        }
    }
}