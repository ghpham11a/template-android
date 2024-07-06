package com.example.template.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayOfWeekSelector(selectedDay: LocalDate, days: List<LocalDate>, onDaySelected: (LocalDate) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(days) { day ->
            Button(
                onClick = {
                    onDaySelected(day)
                },
                colors = ButtonDefaults.buttonColors(if (day == selectedDay) Color.Green else Color.LightGray),
                elevation = null,
                shape = RoundedCornerShape(0),
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(0))

            ) {
                Text(text = day.dayOfMonth.toString(), fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}