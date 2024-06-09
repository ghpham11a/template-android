package com.example.template.ui.screens.thing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThingDescriptionScreen(stepNumber: Int, stepDescription: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        // modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Thing Description")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stepDescription)
    }
}