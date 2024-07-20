package com.example.template.ui.screens.availability

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import com.example.template.ui.components.misc.DayOfWeekSelector
import com.example.template.ui.screens.auth.CodeVerificationViewModel
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(navController: NavController) {

    val selectedDay = remember { mutableStateOf(LocalDate.now()) }

    var isAvailabilityTypeSelectorExpanded by remember { mutableStateOf(false) }

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
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Next two weeks")
            DayOfWeekSelector(
                selectedDay = selectedDay.value,
                days = listOf(
                    LocalDate.parse("2022-12-01"),
                    LocalDate.parse("2022-12-02"),
                    LocalDate.parse("2022-12-03"),
                    LocalDate.parse("2022-12-04"),
                    LocalDate.parse("2022-12-05"),
                    LocalDate.parse("2022-12-06")
                ),
                onDaySelected = {
                    selectedDay.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(selectedDay.value.toString())

            ExposedDropdownMenuBox(
                expanded = isAvailabilityTypeSelectorExpanded,
                onExpandedChange = { isAvailabilityTypeSelectorExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "selectedCountryCode",
                    onValueChange = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable { isAvailabilityTypeSelectorExpanded = !isAvailabilityTypeSelectorExpanded },
                    label = { Text("Availability Type") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    readOnly = true
                )

                ExposedDropdownMenu(
                    expanded = isAvailabilityTypeSelectorExpanded,
                    onDismissRequest = { isAvailabilityTypeSelectorExpanded = false }
                ) {
                    Constants.COUNTRY_CODES.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                // onCountryCodeChange(selectionOption)
                                isAvailabilityTypeSelectorExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}