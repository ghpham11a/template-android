package com.example.template.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.R
import com.example.template.Screen
import com.example.template.ui.components.buttons.HorizontalIconButton
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.buttons.TextButton
import com.example.template.ui.components.images.GlideImage
import com.example.template.ui.components.texts.HeadingText
import com.example.template.utils.Constants
import com.example.template.utils.Constants.USER_IMAGE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController) {

    val viewModel = hiltViewModel<ProfileViewModel>()
    val userSub by viewModel.userSub.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val cells by viewModel.cells.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (isLoggedIn) {

            HorizontalIconButton(
                icon = {
                    if (userSub != "") {
                        GlideImage(
                            model = String.format(USER_IMAGE_URL, userSub),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Add Photo",
                                tint = Color.White
                            )
                        }
                    }

                },
                contentDescription = "Current User",
                title = "Anthony",
                onClick = {
                    navController.navigate(Screen.PublicProfile.build(viewModel.userSub.value))
                },
                subTitle = "View Profile"
            )

            HeadingText(text = "Settings")

            HorizontalIconButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                },
                contentDescription = "Add",
                title = "Personal Information",
                onClick = {
                    navController.navigate(Screen.PersonalInfo.route)
                }
            )

            HorizontalDivider()

            HorizontalIconButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                },
                contentDescription = "Add",
                title = "Login & Security",
                onClick = {
                    navController.navigate(Screen.LoginAndSecurity.route)
                }
            )

            HorizontalDivider()

            HorizontalIconButton(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_payments_hub),
                        contentDescription = "Add",
                        modifier = Modifier.size(35.dp),
                        tint = Color.Black
                    )
                },
                contentDescription = "Add",
                title = "Payments & Payouts",
                onClick = {
                    navController.navigate(Screen.PaymentsHub.route)
                }
            )

            HorizontalDivider()

            HorizontalIconButton(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_payments_hub),
                        contentDescription = "Add",
                        modifier = Modifier.size(35.dp),
                        tint = Color.Black
                    )
                },
                contentDescription = "Add",
                title = "Availability",
                onClick = {
                    navController.navigate(Screen.Availability.route)
                }
            )

            HorizontalDivider()

            TextButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.logOut()
                    }
                },
                buttonText = "Log Out"
            )

        } else {

            HeadingText(text = "Your Profile")
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Log in to find help or start helping others")

            Button(
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    navController.navigate(Screen.AuthHub.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Log in or sign up")
            }
        }
    }
}