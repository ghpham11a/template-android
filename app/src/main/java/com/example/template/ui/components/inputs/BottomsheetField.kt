package com.example.template.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomsheetField(
    isExpanded: Boolean,
    isEnabled: Boolean,
    title: String,
    onExpandedChange: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (isExpanded) {
        ModalBottomSheet(
            onDismissRequest = {
                onExpandedChange(false)
            },
            sheetState = sheetState,
            modifier =  Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.5f),
        ) {
            // Sheet content
            content()
        }
    }

    Column {
        Button(
            onClick = {
                onExpandedChange(true)
            },
            shape = MaterialTheme.shapes.large.copy(all = CornerSize(8.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = null,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = title,
                color = if (isEnabled) Color.Black else Color.Gray,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Chevron Right",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}