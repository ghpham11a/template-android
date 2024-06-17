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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
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

    val viewModel = hiltViewModel<ThingViewModel>()

    viewModel.updatePageCount(steps.split(",").size)

    val pageCount by viewModel.pageCount.collectAsState()
    val pagerState = rememberPagerState(pageCount = { pageCount })

    val coroutineScope = rememberCoroutineScope()

    val stepsMap = steps.split(",").withIndex().associate { it.index to it.value }

    val isNextEnabled by viewModel.isNextEnabled.collectAsState()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()

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
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (stepsMap[page]) {
                    Constants.ThingScreen.THING_TYPE -> {
                        ThingTypeScreen(viewModel = viewModel)
                    }

                    Constants.ThingScreen.THING_DESCRIPTION -> {
                        ThingDescriptionScreen(viewModel = viewModel)
                    }

                    Constants.ThingScreen.THING_METHODS -> {
                        ThingMethodsScreen(viewModel = viewModel)
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
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }, modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(50.dp)
                    ) {
                        Text("Previous")
                    }
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (pagerState.currentPage < pagerState.pageCount - 1) {

                    LoadingButton(
                        onClick = {
                            viewModel.updateCurrentPage(pagerState.currentPage + 1)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(50.dp),
                        isLoading = false,
                        buttonText = "Next",
                        disabled = !isNextEnabled
                    )

                } else {
                    LoadingButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .height(50.dp),
                        isLoading = false,
                        buttonText = "Save",
                        disabled = !isSaveEnabled
                    )
                }
            }
        }
    }
}