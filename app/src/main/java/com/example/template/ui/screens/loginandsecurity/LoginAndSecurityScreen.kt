package com.example.template.ui.screens.loginandsecurity

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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginAndSecurityScreen(navController: NavController) {

    val viewModel = hiltViewModel<LoginAndSecurityViewModel>()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    var isPasswordExpanded by remember { mutableStateOf(false) }

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
                title = "Section 2",
                isExpanded = isPasswordExpanded,
                onExpandedChange = { isPasswordExpanded = it },
                closedContent = {
                    Text("Header content of section 2.")
                },
                openedContent = {
                    var currentPassword by remember { mutableStateOf(TextFieldValue()) }
                    var newPassword by remember { mutableStateOf(TextFieldValue()) }
                    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }
                    Column {
                        Text("Body content of section 2.")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LoadingButton(
                            onClick = {
                                viewModel.changePassword(currentPassword.text, newPassword.text) { response ->
                                    coroutineScope.launch(Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            isPasswordExpanded = false
                                        }
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

            TextButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.disableUser()
                    }
                },
                buttonText = "Deactivate Account"
            )

            TextButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.deleteUser()
                    }
                },
                buttonText = "Delete Account"
            )
        }
    }
}