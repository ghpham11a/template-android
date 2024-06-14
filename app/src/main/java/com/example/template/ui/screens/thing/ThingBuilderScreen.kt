package com.example.template.ui.screens.thing

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.utils.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ThingBuilderScreen(
    navController: NavController,
    mode: String,
    steps: String,
    closeButton: @Composable () -> Unit,
) {

    val pagerState = rememberPagerState(pageCount = { steps.split(",").count() })
    val coroutineScope = rememberCoroutineScope()

    val stepsMap = steps.split(",").withIndex().associate { it.index to it.value }

    val isNextOrSavedEnabled = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            closeButton()
            if (mode == "SCREEN") {
                TextButton(
                    onClick = {
                        navController.navigateUp()
                    },
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
             horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (stepsMap[page]) {
                    Constants.ThingScreen.THING_TYPE -> {
                        ThingTypeScreen(stepNumber = page + 1, stepDescription = "Thing Type")
                    }
                    Constants.ThingScreen.THING_DESCRIPTION -> {
                        ThingDescriptionScreen(stepNumber = page + 1, stepDescription = "Thing Description")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 0 until pagerState.pageCount) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .background(if (i <= pagerState.currentPage) MaterialTheme.colorScheme.primary else Color.Gray)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                if (pagerState.currentPage > 0) {
                    Button(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }, modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .height(50.dp)) {
                        Text("Previous")
                    }
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    Button(onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }, modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .height(50.dp),
                        enabled = isNextOrSavedEnabled.value) {
                        Text("Next")
                    }
                } else {
                    Button(onClick = {
                        coroutineScope.launch {

                        }
                    }, modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .height(50.dp),
                        enabled = isNextOrSavedEnabled.value) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun StepView(stepNumber: Int, stepDescription: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        // modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Step $stepNumber")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stepDescription)
    }
}

@Composable
fun StepProgressBar(
    totalSteps: Int,
    completedSteps: Int,
    modifier: Modifier = Modifier,
    completedColor: Color = Color.Green,
    pendingColor: Color = Color.Gray,
    barHeight: Int = 8
) {
    Row(
        modifier = modifier
            .height(barHeight.dp)
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalSteps) { step ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(barHeight.dp)
                    .background(if (step < completedSteps) completedColor else pendingColor)
            )
        }
    }
}