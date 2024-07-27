package com.example.template.ui.screens.features

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
fun FeatureCard(
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Card(

    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().padding(0.dp).background(Color.Transparent),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
                Text(title, color = Color.Black)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(description, color = Color.Black)
            }
        }
    }
}