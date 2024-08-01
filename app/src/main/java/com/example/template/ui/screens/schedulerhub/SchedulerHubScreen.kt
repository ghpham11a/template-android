package com.example.template.ui.screens.schedulerhub

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.screens.chathub.ChatHubViewModel
import com.example.template.ui.screens.videocallhub.VideoCallHubViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulerHubScreen(navController: NavController) {

    val viewModel = hiltViewModel<SchedulerHubViewModel>()
    val users by viewModel.users.collectAsState()
    var coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
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

            Spacer(modifier = Modifier.padding(32.dp))

            LazyColumn {
                itemsIndexed(users) { index, user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Text(
                                text = user.preferredName ?: user.firstName ?: "",
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.padding(4.dp))

                            LoadingButton(
                                isLoading = false,
                                onClick = {
                                    navController.navigate(Screen.Scheduler.build(user.userId ?: "", "1"))
                                },
                                buttonText = "Scheduler Type 1",
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.padding(4.dp))

                            LoadingButton(
                                isLoading = false,
                                onClick = {
                                    navController.navigate(Screen.Scheduler.build(user.userId ?: "", "2"))
                                },
                                buttonText = "Scheduler Type 2",
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.padding(4.dp))

                            LoadingButton(
                                isLoading = false,
                                onClick = {
                                    navController.navigate(Screen.Scheduler.build(user.userId ?: "", "3"))
                                },
                                buttonText = "Scheduler Type 3",
                                modifier = Modifier.fillMaxWidth(),
                            )

                            Spacer(modifier = Modifier.padding(4.dp))

                            LoadingButton(
                                isLoading = false,
                                onClick = {
                                    navController.navigate(Screen.Scheduler.build(user.userId ?: "", "4"))
                                },
                                buttonText = "Scheduler Type 4",
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                }
            }
        }
    }
}