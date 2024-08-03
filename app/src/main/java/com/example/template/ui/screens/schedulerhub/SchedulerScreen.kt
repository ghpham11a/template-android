package com.example.template.ui.screens.schedulerhub

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.misc.Tag
import com.example.template.ui.screens.chathub.ChatHubViewModel
import com.example.template.ui.screens.features.FeatureCard
import com.example.template.ui.screens.videocallhub.VideoCallHubViewModel
import com.example.template.utils.toHourMinuteString
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SchedulerScreen(navController: NavController, userId: String, availabilityType: String) {

    val viewModel = hiltViewModel<SchedulerViewModel>()

    val durationOptions by viewModel.durationOptions.collectAsState()
    val duration by viewModel.duration.collectAsState()

    val schedulingOptions by viewModel.schedulingOptions.collectAsState()
    val schedulingMethod by viewModel.schedulingMethod.collectAsState()

    val timeOptions by viewModel.timeOptions.collectAsState()
    val selectedTime by viewModel.time.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUser(userId, availabilityType)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        navController.navigateUp()
                    },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                }
                TextButton(
                    onClick = {
                        navController.navigate(Screen.Conflicts.build(userId, availabilityType))
                    },
                ) {
                    Text(text = "Add conflicts")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.padding(32.dp))

            Text(text = "How long?")

            Spacer(modifier = Modifier.padding(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                durationOptions.forEach { tag ->
                    Button(
                        onClick = {
                            viewModel.onDurationChange(tag)
                        },
                        colors = if (duration == tag) {
                            ButtonDefaults.buttonColors(Color.Green)
                        } else {
                            ButtonDefaults.buttonColors(Color.Blue)
                        }
                    ) {
                        Text(text = "${tag.toString()} Minutes")
                    }
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Text(text = "What type?")

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                schedulingOptions.forEach { tag ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.onSchedulingMethodChange(tag)
                        },
                        colors = if (schedulingMethod == tag) {
                            ButtonDefaults.buttonColors(Color.Green)
                        } else {
                            ButtonDefaults.buttonColors(Color.Blue)
                        }
                    ) {
                        Text(text = tag.toString())
                    }
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Text(text = "What time?")

            Spacer(modifier = Modifier.padding(8.dp))

            LazyColumn(modifier = Modifier
                .fillMaxWidth()) {
                items(timeOptions) { item ->
                    Text(text = item.date)

                    Spacer(modifier = Modifier.padding(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item.times.forEach { timesBlock ->
                            timesBlock.forEach { time ->
                                Button(
                                    onClick = {
                                        viewModel.onTimeChange(time)
                                    },
                                    enabled = time.isSelectable,
                                    colors = if (!time.isSelectable) {
                                        ButtonDefaults.buttonColors(Color.Gray)
                                    } else if (time.start.toHourMinuteString() == selectedTime.start.toHourMinuteString()) {
                                        ButtonDefaults.buttonColors(Color.Green)
                                    } else {
                                        ButtonDefaults.buttonColors(Color.Blue)
                                    }
                                ) {
                                    Text(text = "${time}")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }

        }
    }
}