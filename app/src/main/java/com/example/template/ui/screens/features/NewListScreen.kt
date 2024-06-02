package com.example.template.ui.screens.features

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewListScreen(navController: NavController) {

    val list = listOf("New Item 1", "New Item 2", "New Item 3")

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list) { item ->
            Text(text = item)
        }
    }
}