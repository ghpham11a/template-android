package com.example.template.ui.screens.addpayout

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.get
import com.amazonaws.mobile.client.results.SignInState
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddBankInfoScreen(navController: NavController, country: String) {

    val viewModel = hiltViewModel<AddBankInfoViewModel>()

    val isLoading by viewModel.isLoading.collectAsState()

    var accountHolderName by remember { mutableStateOf("John Doe") }
    var routingNumber by remember { mutableStateOf("110000000") }
    var accountNumber by remember { mutableStateOf("000123456789") }
    var accountNumberConfirmation by remember { mutableStateOf("000123456789") }

    var coroutineScope = rememberCoroutineScope()

    fun getCountryCurrency(country: String): String {
        return when (country) {
            "US" -> "USD"
            else -> ""
        }
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
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text("Account holder name")

            OutlinedTextField(
                value = accountHolderName,
                onValueChange = { accountHolderName = it },
                label = { Text("Account holder name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Routing number")

            OutlinedTextField(
                value = routingNumber,
                onValueChange = { routingNumber = it },
                label = { Text("Routing number") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Account number")

            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = accountNumberConfirmation,
                onValueChange = { accountNumberConfirmation = it },
                label = { Text("Confirm account number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.Main) {
                        val response = viewModel.handleContinue(country, getCountryCurrency(country), accountHolderName, routingNumber, accountNumber)
                        if (response != null) {
                            navController.popBackStack(navController.graph[Screen.PayoutMethods.route].id, false)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                buttonText = "Save"
            )
        }
    }
}

