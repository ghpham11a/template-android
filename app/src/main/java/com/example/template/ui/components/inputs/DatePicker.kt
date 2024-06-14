package com.example.template.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@Composable
fun DatePicker(
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedYear by remember { mutableStateOf(year) }
    var selectedMonth by remember { mutableStateOf(month) }
    var selectedDay by remember { mutableStateOf(day) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Select Date")

                Spacer(modifier = Modifier.height(8.dp))

                AndroidView(
                    factory = { context ->
                        android.widget.DatePicker(context).apply {
                            init(selectedYear, selectedMonth, selectedDay) { _, year, monthOfYear, dayOfMonth ->
                                selectedYear = year
                                selectedMonth = monthOfYear
                                selectedDay = dayOfMonth
                            }
                        }
                    },
                    update = { view ->
                        view.updateDate(selectedYear, selectedMonth, selectedDay)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onDateSelected(String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay))
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}