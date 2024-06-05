package com.example.template.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.amazonaws.mobile.client.results.SignInState
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.utils.Constants
import com.example.template.utils.Constants.USER_NOT_CONFIRMED_INDICATOR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPasswordScreen(navController: NavController, username: String) {

    val TAG = "EnterPasswordScreen"

    val coroutineScope = rememberCoroutineScope()

    val viewModel = hiltViewModel<EnterPasswordViewModel>()

    val password by viewModel.password.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter Password for $username",
                fontSize = 24.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                                },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                onClick = {
                    viewModel.signIn(username, password) { response ->
                        coroutineScope.launch(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                if (response.result?.signInState == SignInState.DONE) {
                                    navController.popBackStack(Constants.Route.AUTH, true)
                                }
                            } else {
                                if (response.exception?.localizedMessage?.contains(USER_NOT_CONFIRMED_INDICATOR) == true) {
                                    navController.navigate(String.format(Constants.Route.CODE_VERIFICATION, username, password))
                                } else {
                                    navController.popBackStack(Constants.Route.AUTH, true)
                                    navController.navigate(String.format(Constants.Route.SNAG_FORMAT, "An error occurred. Please try again."))
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                buttonText = "Sign in"
            )

        }
    }
}