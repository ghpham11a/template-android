package com.example.template.ui.screens.thing

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.Screen
import com.example.template.ui.components.inputs.CheckboxRow
import com.example.template.ui.screens.publicprofile.PublicProfileViewModel
import com.example.template.utils.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ThingScreen(navController: NavController) {

    val viewModel = hiltViewModel<ThingViewModel>()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var isThingTypeChecked by remember { mutableStateOf(false) }
    var isThingDescriptionChecked by remember { mutableStateOf(false) }
    var isThingMethodsChecked by remember { mutableStateOf(false) }

    fun getStepString(): String {
        val steps = mutableListOf<String>()
        if (isThingTypeChecked) {
            steps.add(Constants.ThingScreen.THING_TYPE)
        }
        if (isThingDescriptionChecked) {
            steps.add(Constants.ThingScreen.THING_DESCRIPTION)
        }
        if (isThingMethodsChecked) {
            steps.add(Constants.ThingScreen.THING_METHODS)
        }
        return steps.joinToString(separator = ",")
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            modifier = Modifier
                .padding(0.dp)
                .height(LocalConfiguration.current.screenHeightDp.dp * 0.9f),
        ) {
            ThingBuilderScreen(
                navController = navController,
                mode = "BOTTOM_SHEET",
                steps = getStepString(),
                closeButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            )
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Steps Guide")

            CheckboxRow(
                title = "Thing type",
                isThingTypeChecked.also { isThingTypeChecked = it },
                onCheckedChange = {
                    isThingTypeChecked = it
                }
            )

            CheckboxRow(
                title = "Thing type description",
                isThingDescriptionChecked.also { isThingDescriptionChecked = it },
                onCheckedChange = {
                    isThingDescriptionChecked = it
                }
            )

            CheckboxRow(
                title = "Thing methods",
                isThingMethodsChecked.also { isThingMethodsChecked = it },
                onCheckedChange = {
                    isThingMethodsChecked = it
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(Screen.ThingBuilder.build("SCREEN", getStepString()))
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Launch in screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    showBottomSheet = true
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Launch in bottom sheet")
            }
        }
    }
}