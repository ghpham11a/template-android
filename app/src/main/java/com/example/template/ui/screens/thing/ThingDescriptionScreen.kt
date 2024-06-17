package com.example.template.ui.screens.thing

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThingDescriptionScreen(viewModel: ThingViewModel) {

    val thing by viewModel.thing.collectAsState()
    var wordCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.onDescriptionChange(viewModel.thing.value.thingDescription ?: "")
    }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        Text("Enter your text:", style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp))

        BasicTextField(
            value = thing.thingDescription ?: "",
            onValueChange = { newText ->
                val words = newText.split("\\s+".toRegex()).filter { it.isNotEmpty() }
                if (words.size <= 200) {
                    viewModel.onDescriptionChange(newText)
                    wordCount = words.size
                } else {
                    viewModel.onDescriptionChange(newText)
                    wordCount = 200
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, Color.Gray)
                .fillMaxWidth()
                .height(200.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Word count: $wordCount/200",
            style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = if (wordCount > 200) Color.Red else Color.Black)
        )
    }
}