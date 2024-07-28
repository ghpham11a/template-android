package com.example.template.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.template.utils.Constants
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DayOfWeekSelector(
    dates: List<LocalDate>,
    selectedDay: LocalDate,
    selectedIndex: Int,
    onDaySelected: (LocalDate) -> Unit,
) {
    var currentIndex by remember { mutableStateOf(selectedIndex) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = {
            if (currentIndex > 0) {
                currentIndex--
                onDaySelected(dates[currentIndex])
            }
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day")
        }



        if (dates.isNotEmpty()) {
            dates.get(currentIndex)?.let {
                Text(text = "${it.format(formatter)}")
            }
        }

//        dates.get(currentIndex)?.let {
//            Text(
//                text = format.format(it),
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//        }

//        dates[currentIndex]?.let {
//
//        }
//
//        Text(
//            text = format.format(dates[currentIndex]),
//        )

        IconButton(onClick = {
            if (currentIndex < dates.size - 1) {
                currentIndex++
                onDaySelected(dates[currentIndex])
            }
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day")
        }
    }
}

//fun DayOfWeekSelector(selectedDay: LocalDate, days: List<LocalDate>, onDaySelected: (LocalDate) -> Unit) {
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(3.dp)
//    ) {
//        items(days) { day ->
//            Button(
//                onClick = {
//                    onDaySelected(day)
//                },
//                colors = ButtonDefaults.buttonColors(if (day == selectedDay) Color.Green else Color.LightGray),
//                elevation = null,
//                shape = RoundedCornerShape(0),
//                modifier = Modifier
//                    .width(100.dp)
//                    .height(150.dp)
//                    .border(1.dp, Color.Black, RoundedCornerShape(0))
//
//            ) {
//                Text(text = day.dayOfMonth.toString(), fontWeight = FontWeight.Bold, color = Color.Black)
//            }
//        }
//    }
//}