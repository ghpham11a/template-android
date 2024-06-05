package com.example.template.ui.screens.features

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.utils.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeaturesScreen(navController: NavController) {

    var tabIndex by remember { mutableStateOf(0) }
    val viewModel = hiltViewModel<FeaturesViewModel>()
    val tabs = listOf("New", "Old")
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text("Features")
        }

        if (!isLoggedIn) {
            Spacer(modifier = Modifier.padding(8.dp))

            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    navController.navigate(Constants.Route.AUTH)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Log in")
            }
        } else {
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 200.dp),
                divider = { }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            HorizontalDivider(
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            when (tabIndex) {
                0 -> NewListScreen(navController, viewModel)
                1 -> OldListScreen(navController, viewModel)
            }
        }
    }
}