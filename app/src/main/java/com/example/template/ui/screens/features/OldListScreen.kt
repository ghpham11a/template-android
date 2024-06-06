package com.example.template.ui.screens.features

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.template.utils.Constants

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OldListScreen(navController: NavController, viewModel: FeaturesViewModel) {

    val list by viewModel.oldItems.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)) {
        items(list) { item ->
            FeatureCard(title = item.title, description = item.description, onClick = {
                navController.navigate(Constants.Route.STEPS_GUIDE)
            })
            if (item != list.last()) {
                HorizontalDivider()
            }
        }
    }
}