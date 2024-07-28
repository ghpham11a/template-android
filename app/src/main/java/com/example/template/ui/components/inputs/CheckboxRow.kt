package com.example.template.ui.components.inputs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxRow(title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Button(
        onClick = {
            onCheckedChange(!isChecked)
        },
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = Color.Black)
            Checkbox(
                checked = isChecked,
                onCheckedChange = {  },
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}