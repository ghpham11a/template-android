package com.example.template.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.*

@Composable
fun TimePickerField(label: String, time: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(onClick = onClick) {
            Text(text = if (time.isEmpty()) "Select Time" else time)
        }
    }
}

@Composable
fun NumberPicker(value: Int, onValueChange: (Int) -> Unit, range: List<Int>) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp)
            .background(Color.LightGray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(range) { i ->
                Text(
                    text = i.toString(),
                    modifier = Modifier
                        .padding(4.dp)
                        .background(if (i == value) Color.Blue else Color.Transparent)
                        .padding(4.dp)
                        .clickable { onValueChange(i) }
                )
            }
        }
    }
}

@Composable
fun TimePickerDialog(onTimeSelected: (String) -> Unit) {
    Dialog(onDismissRequest = { }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                var hour by remember { mutableStateOf(12) }
                var minute by remember { mutableStateOf(0) }

                Text(text = "Select Time", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberPicker(
                        value = hour,
                        onValueChange = { hour = it },
                        range = (0..23).toList()
                    )
                    Text(text = ":")
                    NumberPicker(
                        value = minute,
                        onValueChange = { minute = it },
                        range = listOf(0, 15, 30, 45)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onTimeSelected(String.format("%02d:%02d", hour, minute))
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}