package com.example.template.ui.screens.xmlview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.R
import com.example.template.ui.screens.publicprofile.PublicProfileViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun XMLViewScreen(navController: NavController) {

    val viewModel = hiltViewModel<XMLViewModel>()

    val textContent by viewModel.textContent.collectAsState()

    val couroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TextButton(
                onClick = {
                    navController.navigateUp()
                },
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }
    ) {
        Column {

            Spacer(modifier = Modifier.height(32.dp))

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    LayoutInflater.from(context).inflate(R.layout.xml_view_screen, null, false)
                },
                update = { view ->
                    val button = view.findViewById<Button>(R.id.button_make_api_call)
                    val text = view.findViewById<TextView>(R.id.text_api_response)

                    text.text = textContent

                    button.setOnClickListener {
                        // Handle button click here
                        couroutineScope.launch {
                            viewModel.makeAPICall()
                        }
                    }
                }
            )
        }
    }
}