package com.example.template.ui.components.inputs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableSection(
    title: String,
    isExpanded: Boolean,
    isEnabled: Boolean = true,
    onExpandedChange: (Boolean) -> Unit,
    closedContent: @Composable () -> Unit,
    openedContent: @Composable () -> Unit
) {
    val textColor = if (isEnabled) Color.Black else Color.Gray

    Column(
        modifier = Modifier
            .animateContentSize(animationSpec = tween(300))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 20.sp, color = textColor)
            Button(
                onClick = { if (isEnabled) onExpandedChange(!isExpanded) },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(if (isExpanded) "Cancel" else "Edit", color = textColor)
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        ) {
            if (!isExpanded) {
                CompositionLocalProvider(LocalContentColor provides textColor) {
                    closedContent()
                }
            } else {
                CompositionLocalProvider(LocalContentColor provides textColor) {
                    openedContent()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()
    }
}