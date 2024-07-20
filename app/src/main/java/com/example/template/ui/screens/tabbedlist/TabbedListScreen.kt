package com.example.template.ui.screens.tabbedlist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.template.Screen
import com.example.template.ui.screens.features.FeatureCard
import com.example.template.ui.screens.features.FeaturesViewModel
import com.example.template.utils.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TabbedListScreen(navController: NavController) {

    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("List A", "List B")

    val listAItems by remember {
        mutableStateOf(
            listOf(
                "List A Item 1",
                "List A Item 2",
                "List A Item 3",
            )
        )
    }

    val listBItems by remember {
        mutableStateOf(
            listOf(
                "List B Item 1",
                "List B Item 2",
                "List B Item 3",
                "List B Item 4",
                "List B Item 5",
                "List B Item 6",
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {

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
            0 -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)) {
                    items(listAItems) { item ->
                        Text(item)
                        if (item != listAItems.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
            1 -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)) {
                    items(listBItems) { item ->
                        Text(item)
                        if (item != listBItems.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}