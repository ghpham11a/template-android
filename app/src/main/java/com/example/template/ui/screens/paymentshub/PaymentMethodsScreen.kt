package com.example.template.ui.screens.paymentshub

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.template.ui.components.buttons.HorizontalIconButton
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.texts.HeadingText
import com.example.template.ui.screens.auth.CodeVerificationViewModel
import com.example.template.ui.screens.filterlist.FilterListViewModel
import com.example.template.utils.Constants
import com.stripe.android.payments.paymentlauncher.PaymentResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(navController: NavController) {

    val viewModel = hiltViewModel<PaymentMethodsViewModel>()
    val isLoading by viewModel.isLoading.collectAsState()

    val paymentMethods by viewModel.paymentMethods.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val paymentSheet = rememberPaymentSheet { paymentResult ->
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                // showToast("Payment complete!")
                coroutineScope.launch {
                    viewModel.fetchPaymentMethods()
                }
            }

            is PaymentSheetResult.Canceled -> {
                // showToast("Payment canceled!")
            }

            is PaymentSheetResult.Failed -> {
                Log.d("PaymentMethodScreen", "Payment failed: ${paymentResult.error}")
            }
        }
    }

    fun onPayClicked(
        paymentSheet: PaymentSheet,
        stripeCustomerId: String,
        stripeEphemeralKey: String,
        setupIntentClientSecret: String,
    ) {
        val configuration = PaymentSheet.Configuration.Builder(merchantDisplayName = "Template")
            .customer(PaymentSheet.CustomerConfiguration(stripeCustomerId, stripeEphemeralKey))
            .build()
        paymentSheet.presentWithSetupIntent(setupIntentClientSecret, configuration)
    }

    Scaffold(
        topBar = {
            TextButton(
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
                .fillMaxSize()
                .padding(16.dp),
        ) {

            Spacer(modifier = Modifier.padding(32.dp))

            HeadingText(text = "Payment Methods")

            if (paymentMethods.isEmpty()) {
                Text(
                    text = "No payment methods added",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    items(paymentMethods) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = it.brand ?: "")
                            Column {
                                Text(text = it.last4 ?: "")
                                Text(text = it.expMonth.toString() + "/" + it.expYear.toString())
                            }
                            Spacer(modifier = Modifier.width(16.dp))

                            OutlinedButton(onClick = {

                            }) {
                                Text("Edit")
                            }
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.Main) {
                        viewModel.createSetupIntent()?.let {
                            onPayClicked(
                                paymentSheet,
                                it.stripeCustomerId ?: "",
                                it.ephemeralKey ?: "",
                                it.setupIntent ?: ""
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = isLoading,
                buttonText = "Add payment method"
            )
        }
    }
}
