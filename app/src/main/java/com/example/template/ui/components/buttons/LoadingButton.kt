package com.example.template.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit,
    buttonText: String,
    disabled: Boolean = false
) {
    Button(
        shape = MaterialTheme.shapes.medium,
        onClick = { if (!isLoading && !disabled) onClick() },
        modifier = modifier.height(48.dp),
        enabled = !disabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(text = buttonText)
        }
    }
}