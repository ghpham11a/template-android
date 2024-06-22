package com.example.template.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.inputs.CodeInputField
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CodeVerificationScreen(
    navController: NavController,
    verificationType: String,
    username: String,
    password: String
) {

    val numberOfFields = 6
    val focusRequesters = remember { List(numberOfFields) { FocusRequester() } }
    val verificationCode = remember { mutableStateOf(List(numberOfFields) { "" }) }
    val viewModel = hiltViewModel<CodeVerificationViewModel>()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TextButton(
                onClick = {
                    navController.navigateUp()
                },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text("Enter the verification code sent to your email ${username}", style = MaterialTheme.typography.headlineMedium)

            CodeInputField(verificationCode, focusRequesters, 6)

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                onClick = {
                    // Handle verification code submission
                    verificationCode.value.joinToString("")
                    val code = verificationCode.value.joinToString("")

                    if (verificationType == "SIGN_UP") {
                        viewModel.confirmSignUp(username, password, code) { response ->
                            coroutineScope.launch(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    navController.popBackStack(Screen.AuthHub.route, true)
                                }
                            }
                        }
                    }

                    if (verificationType == "RESET_PASSWORD") {
                        navController.navigate(Screen.NewPassword.build(username, code))
                    }
                },
                buttonText = "Verify"
            )

            Spacer(modifier =  Modifier.height(16.dp))

            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                isLoading = isSending,
                onClick = {
                    // Handle verification code submission
                    verificationCode.value.joinToString("")
                    val code = verificationCode.value.joinToString("")
                    viewModel.resendConfirmationCode(username) { response ->
                        coroutineScope.launch(Dispatchers.Main) {
                            if (response.isSuccessful) {

                            } else {

                            }
                        }
                    }
                },
                buttonText = "Resend Code"
            )
        }
    }
}