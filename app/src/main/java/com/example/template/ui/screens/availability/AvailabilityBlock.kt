package com.example.template.ui.screens.availability

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.template.ui.components.inputs.TimePickerDialog
import com.example.template.ui.components.inputs.TimePickerField
import java.util.*

@Composable
fun AvailabilityBlock(initStartTime: String, initEndTime: String) {
    var startTime by remember { mutableStateOf(initStartTime) }
    var endTime by remember { mutableStateOf(initEndTime) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimePickerField("Start Time", startTime) { showStartTimePicker = true }
            TimePickerField("End Time", endTime) { showEndTimePicker = true }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    if (showStartTimePicker) {
        TimePickerDialog(onTimeSelected = { time ->
            startTime = time
            showStartTimePicker = false
        })
    }

    if (showEndTimePicker) {
        TimePickerDialog(onTimeSelected = { time ->
            endTime = time
            showEndTimePicker = false
        })
    }
}