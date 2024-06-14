package com.example.template.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.R
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.buttons.OutlinedIconButton
import com.example.template.ui.components.inputs.PhoneNumberField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.buildAnnotatedString as buildAnnotatedString1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthHubScreen(navController: NavController) {

    val TAG = "AuthScreen"

    val viewModel = hiltViewModel<AuthHubViewModel>()

    var expanded by remember { mutableStateOf(false) }

    val username by viewModel.username.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isPhoneNumberMode by viewModel.isPhoneNumberMode.collectAsState()

    val selectedCountryCode by viewModel.selectedCountryCode.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val resetPasswordPhrase = "Reset it"
    val forgotPasswordQuestion = "Forgot your password? $resetPasswordPhrase."

    val annotatedString = buildAnnotatedString1 {
        append(forgotPasswordQuestion)

        addStyle(
            style = SpanStyle(

                color = Color.Blue,
                fontWeight = FontWeight.Bold
            ),
            start = forgotPasswordQuestion.indexOf(resetPasswordPhrase),
            end = forgotPasswordQuestion.indexOf(resetPasswordPhrase) + resetPasswordPhrase.length
        )

        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.example.com",
            start = 25,
            end = 35
        )
    }

    Scaffold(
        topBar = {
            TextButton(
                onClick = {
                    navController.navigateUp()
                },
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login or sign up",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isPhoneNumberMode) {

                PhoneNumberField(
                    selectedCountryCode = selectedCountryCode,
                    onCountryCodeChange = { viewModel.onCountryCodeChange(it) },
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { viewModel.onPhoneNumberChange(it) },
                )

            } else {
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {

                        if (isPhoneNumberMode) {

                            val code = "+${(selectedCountryCode.filter { it.isDigit() })}"

                            val formattedPhoneNumber = code + phoneNumber.filter { it.isDigit() }

                            if (viewModel.checkIfUserExists(phoneNumber = formattedPhoneNumber)) {
                                withContext(Dispatchers.Main) {
                                    // navController.navigate(Screen.EnterPassword.build(username))
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.AddNewUserInfo.build(phoneNumber = formattedPhoneNumber))
                                }
                            }

                            // navController.navigate(Screen.AddNewUserInfo.build(phoneNumber = phoneNumber))
                        } else {
                            if (viewModel.checkIfUserExists(username = username)) {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.EnterPassword.build(username))
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    navController.navigate(Screen.AddNewUserInfo.build(username = username))
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                buttonText = "Continue"
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isPhoneNumberMode) {
                ClickableText(
                    text = annotatedString,
                    style = LocalTextStyle.current.copy(fontSize = 16.sp),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { _ ->
                                // Handle the click event, e.g., launch a browser with the URL
                                navController.navigate(Screen.ResetPassword.route)
                            }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            if (isPhoneNumberMode) {
                OutlinedIconButton(
                    text = "Continue with Email",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_android_black_24dp),
                    onClick = { viewModel.onPhoneNumberModeChange(false) }
                )
            } else {
                OutlinedIconButton(
                    text = "Continue with Phone",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_android_black_24dp),
                    onClick = { viewModel.onPhoneNumberModeChange(true) }
                )
            }

        }
    }
}