package com.example.template.ui.components.inputs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.Screen
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CodeInputField(
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