package com.example.template.ui.screens.thing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ThingTypeScreen(viewModel: ThingViewModel) {

    val availableTypes by viewModel.availableThingTypes.collectAsState()

    val thing by viewModel.thing.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize()
    ) {
        Text(text = "Thing Type")
        availableTypes.forEach { thingType ->
            Button(
                onClick = {
                    viewModel.selectThingType(thingType)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(if (thing.thingType?.title == thingType.title) Color.LightGray else Color.Transparent),
                elevation = null,
                shape = RoundedCornerShape(0),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(thingType.title, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(thingType.description, color = Color.Black)
                    HorizontalDivider(modifier = Modifier.height(1.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}