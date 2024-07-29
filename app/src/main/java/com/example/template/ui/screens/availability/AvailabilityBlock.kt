package com.example.template.ui.screens.availability


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.template.ui.components.inputs.TimePickerDialog
import com.example.template.ui.components.inputs.TimePickerField

@Composable
fun AvailabilityBlock(
    id: String,
    initStartTime: String,
    initEndTime: String,
    onRemove: () -> Unit,
    onStartTapped: (String) -> Unit,
    onEndTapped: (String) -> Unit,
) {
    var startTime by remember { mutableStateOf(initStartTime) }
    var endTime by remember { mutableStateOf(initEndTime) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Row {
        Column(
            modifier = Modifier
                .weight(.75F)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimePickerField("Start Time", startTime) {
                    onStartTapped(id)
                }
                TimePickerField("End Time", endTime) {
                    onEndTapped(id)
                    // showEndTimePicker = true
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            modifier = Modifier
                .weight(.25F)
                .align(Alignment.CenterVertically),
            onClick = {
                onRemove()
            }
        ) {
            Text("X")
        }
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