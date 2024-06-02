package com.example.template.ui.screens.featurelist

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
fun OldListScreen(navController: NavController) {

    val list = listOf("Old Item 1", "Old Item 2", "Old Item 3")

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list) { item ->
            Text(text = item)
        }
    }
}