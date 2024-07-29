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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.inputs.CheckboxRow
import com.example.template.ui.components.misc.DayOfWeekSelector
import com.example.template.ui.screens.auth.CodeVerificationViewModel
import com.example.template.ui.screens.proxycallhub.ProxyCallHubViewModel
import com.example.template.utils.Constants
import com.example.template.utils.toHourMinuteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(navController: NavController) {

    val viewModel = hiltViewModel<AvailabilityScreenViewModel>()

    var isAvailabilityTypeSelectorExpanded by remember { mutableStateOf(false) }
    var displayedBlocks = viewModel.displayedBlocks.collectAsState()
    var availabilityType = viewModel.availabilityType.collectAsState()
    var isLoading = viewModel.isLoading.collectAsState()
    var days = viewModel.days.collectAsState()

    var selectedDay = viewModel.selectedDay.collectAsState()

    val couroutineScope = rememberCoroutineScope()

    var isTimeSelectorExpanded by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (isTimeSelectorExpanded) {

        val times = if (viewModel.selectedTimeType == "start") {
            viewModel.tapStart()
        } else {
            viewModel.tapEnd()
        }

        ModalBottomSheet(
            onDismissRequest = {
                isTimeSelectorExpanded = false
            },
            sheetState = sheetState,
            modifier =  Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.5f),
        ) {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 48.dp)) {
                items(times) { item ->

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.updateBlock(viewModel.selectedId, viewModel.selectedTimeType, item)
                            couroutineScope.launch {
                                delay(500)
                                isTimeSelectorExpanded = false
                            }
                        }
                    ) {
                        Text(item)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setupBlocks()
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
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                LoadingButton(
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = isLoading.value,
                    onClick = {
                        couroutineScope.launch(Dispatchers.IO) {
                            if (viewModel.updateAvailability()) {
                                withContext(Dispatchers.Main) {
                                    navController.navigateUp()
                                }
                            }
                        }
                    },
                    buttonText = "Save")
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Next two weeks")

                DayOfWeekSelector(
                    selectedDay = selectedDay.value,
                    dates = days.value,
                    selectedIndex = 0,
                    onDaySelected = {
                        viewModel.updateSelectedDay(it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = isAvailabilityTypeSelectorExpanded,
                    onExpandedChange = { isAvailabilityTypeSelectorExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = availabilityType.value,
                        onValueChange = {

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable {
                                isAvailabilityTypeSelectorExpanded =
                                    !isAvailabilityTypeSelectorExpanded
                            },
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
                        listOf<String>("1", "2", "3", "4").forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(text = selectionOption) },
                                onClick = {
                                    viewModel.updateAvailabilityType(selectionOption)
                                    isAvailabilityTypeSelectorExpanded = false
                                }
                            )
                        }
                    }
                }

                displayedBlocks.value.forEach {
                    AvailabilityBlock(
                        id = it.id ,
                        initStartTime = it.startTime.toHourMinuteString(),
                        initEndTime = it.endTime.toHourMinuteString(),
                        onRemove = {
                            viewModel.deleteBlock(it.id ?: "")
                        },
                        onStartTapped = { id ->
                            viewModel.selectedId = id
                            viewModel.selectedTimeType = "start"
                            isTimeSelectorExpanded = true
                        },
                        onEndTapped = { id ->
                            viewModel.selectedId = id
                            viewModel.selectedTimeType = "end"
                            isTimeSelectorExpanded = true
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(onClick = {
                        viewModel.addBlock()
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}