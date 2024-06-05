package com.example.template.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String
) {
    Box(modifier = modifier.padding(0.dp)) {
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = { onClick() },
            modifier = Modifier
                .height(48.dp)
                .padding(0.dp).fillMaxWidth()
                .align(Alignment.TopStart),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text(text = buttonText, color = Color.Red)
        }
    }

}