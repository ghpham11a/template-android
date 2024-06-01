package com.example.template.ui.screens.delta

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.R
import com.example.template.ui.components.loadingbutton.LoadingButton
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeltaScreen(navController: NavController) {

    val viewModel = hiltViewModel<DeltaViewModel>()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val disableIsLoading by viewModel.disableIsLoading.collectAsState()
    val deleteIsLoading by viewModel.deleteIsLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.teal_700))
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "Delta Screen",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            if (isLoggedIn) {

                Button(
                    onClick = { viewModel.logOut() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Logout")
                }

                LoadingButton(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.disableUser()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = disableIsLoading,
                    buttonText = "Deactivate Account"
                )

                LoadingButton(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.deleteUser()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = deleteIsLoading,
                    buttonText = "Delete Account"
                )

            } else {
                Button(
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        navController.navigate(Constants.Route.AUTH)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Log in")
                }
            }
        }
    }
}