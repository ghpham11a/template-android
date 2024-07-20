package com.example.template.ui.screens.videocallhub

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCallHubScreen(navController: NavController) {

    val viewModel = hiltViewModel<VideoCallHubViewModel>()

    val events by viewModel.event.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
        viewModel.fetchAccessToken()
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
                items(events) { event ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = event.user?.preferredName ?: event.user?.firstName ?: "",
                                fontSize = 20.sp
                            )
//                            Row(
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                if (event.proxyCall == null) {
//                                    Button(
//                                        onClick = {
//                                            // Call user
//                                        }
//                                    ) {
//                                        Text("Create masked phone call")
//                                    }
//                                } else {
//                                    Button(
//                                        onClick = {
//                                            // End call
//                                        }
//                                    ) {
//                                        Text("Call")
//                                    }
//                                    Button(
//                                        onClick = {
//                                            // End call
//                                        }
//                                    ) {
//                                        Text("Delete")
//                                    }
//                                }
//                            }
                        }
                    }

                    if (event != events.last()) {
                        HorizontalDivider()
                    }

                }
            }
        }
    }
}