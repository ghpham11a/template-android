package com.example.template.ui.screens.delta

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeltaScreen(navController: NavController) {

    val viewModel = hiltViewModel<DeltaViewModel>()

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
            Button(
                onClick = { viewModel.logOut() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Logout")
            }
        }
    }
}