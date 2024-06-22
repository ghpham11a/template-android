package com.example.template.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CodeField(
    verificationCode: MutableState<List<String>>,
    focusRequesters: List<FocusRequester>,
    numberOfFields: Int
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 0 until numberOfFields) {
            OutlinedTextField(
                value = verificationCode.value[i],
                onValueChange = { value ->
                    if (value.length <= 1) {
                        val newVerificationCode = verificationCode.value.toMutableList()
                        newVerificationCode[i] = value
                        verificationCode.value = newVerificationCode

                        when {
                            value.isNotEmpty() && i < numberOfFields - 1 -> {
                                focusRequesters[i + 1].requestFocus()
                            }
                            value.isEmpty() && i > 0 -> {
                                focusRequesters[i - 1].requestFocus()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .focusRequester(focusRequesters[i]),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (i < numberOfFields - 1) ImeAction.Next else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (i < numberOfFields - 1) {
                            focusRequesters[i + 1].requestFocus()
                        }
                    },
                    onDone = {
                        // Handle the done action, if necessary
                    }
                )
            )

            LaunchedEffect(Unit) {
                if (i == 0) {
                    focusRequesters[i].requestFocus()
                }
            }
        }
    }
}