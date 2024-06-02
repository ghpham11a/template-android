package com.example.template.ui.screens.features

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeaturesScreen(navController: NavController) {

    var tabIndex by remember { mutableStateOf(0) }
    val viewModel = hiltViewModel<FeaturesViewModel>()
    val tabs = listOf("New", "Old")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Features")
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
            0 -> NewListScreen(navController)
            1 -> OldListScreen(navController)
        }
    }
}