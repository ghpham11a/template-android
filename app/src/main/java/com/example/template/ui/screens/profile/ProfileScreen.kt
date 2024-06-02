package com.example.template.ui.screens.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.components.buttons.HorizontalIconButton
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.images.GlideImage
import com.example.template.ui.components.images.UploadImage
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
    val disableIsLoading by viewModel.disableIsLoading.collectAsState()
    val deleteIsLoading by viewModel.deleteIsLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)

    if (!isLoggedIn) {
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = {
                navController.navigate(Constants.Route.AUTH)
            },
        ) {
            Text(text = "Log in")
        }
    } else {

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(list) { item ->
                when (item) {
                    1 -> {
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
                                navController.navigate(String.format(Constants.Route.PUBLIC_PROFILE,viewModel.username))},

                            subTitle = "View Profile"
                        )

                        //UploadImage(uri = Uri.parse("https://template-public-resources.s3.amazonaws.com/This+one.jpg"))
                    }
                    2 -> {
                        Button(
                            onClick = { viewModel.logOut() },
                        ) {
                            Text(text = "Logout")
                        }
                    }
                    3 -> {
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
                    }
                    4 -> {
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
                    }
                    6 -> {
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
                            title = "Add Item",
                            onClick = { /* Handle button click */ }
                        )
                    }
                    7 -> {
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
                            title = "Add Item",
                            onClick = { /* Handle button click */ }
                        )
                    }
                    8 -> {
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
                            title = "Add Item",
                            onClick = { /* Handle button click */ }
                        )
                    }
                }
            }
        }
    }
}