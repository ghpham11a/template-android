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
import com.amazonaws.mobile.client.results.SignInState
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddPayoutScreen(navController: NavController) {

    val viewModel = hiltViewModel<AddPayoutViewModel>()

    var countryDropdownExpanded by remember { mutableStateOf(false) }
    val selectedCountry by viewModel.selectedCountry.collectAsState()

    val availablePayoutTypes by viewModel.availablePayoutTypes.collectAsState()
    val selectedPayoutType by viewModel.selectedPayoutType.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    fun getCountryCode(country: String): String {
        return when (country) {
            "United States" -> "US"
            "Canada" -> "CA"
            "United Kingdom" -> "GB"
            "Australia" -> "AU"
            "New Zealand" -> "NZ"
            else -> ""
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onCountryChange(viewModel.selectedCountry.value)
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
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            Text("Let's add a payout method")

            Text("To start, let us know where you like us to send your money")

            Text("Billing country/region")

            ExposedDropdownMenuBox(
                expanded = countryDropdownExpanded,
                onExpandedChange = { countryDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = {
                        viewModel.onCountryChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable { countryDropdownExpanded = !countryDropdownExpanded },
                    label = { Text("Country Region") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    readOnly = true
                )

                ExposedDropdownMenu(
                    expanded = countryDropdownExpanded,
                    onDismissRequest = { countryDropdownExpanded = false }
                ) {
                    Constants.COUNTRIES.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                viewModel.onCountryChange(selectionOption)
                                countryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Text("This is where you opened your financial account")

            Text("How would you like to get paid?")

            Text("Payouts will be sent in USD")

            availablePayoutTypes.forEach { payoutType ->
                Button(
                    onClick = {
                        viewModel.onPayoutTypeChange(payoutType)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(if (payoutType == selectedPayoutType) Color.LightGray else Color.Transparent),
                    elevation = null,
                    shape = RoundedCornerShape(0),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(payoutType, color = Color.Black)
                        HorizontalDivider(modifier = Modifier.height(1.dp))
                    }
                }
            }

            LoadingButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val response = viewModel.handleContinue()
                        if (response) {
                            withContext(Dispatchers.Main) {
                                navController.navigate(Screen.AddBankInfo.build(getCountryCode(selectedCountry)))
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                buttonText = "Continue"
            )
        }
    }
}