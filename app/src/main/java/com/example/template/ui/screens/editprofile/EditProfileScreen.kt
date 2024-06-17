package com.example.template.ui.screens.editprofile

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.template.ui.components.buttons.HorizontalIconButton
import com.example.template.ui.components.buttons.LoadingButton
import com.example.template.ui.components.images.GlideImage
import com.example.template.ui.components.images.UploadImage
import com.example.template.ui.components.inputs.BottomsheetField
import com.example.template.ui.components.misc.LoadingScreen
import com.example.template.ui.screens.profile.ProfileViewModel
import com.example.template.ui.screens.publicprofile.PublicProfileViewModel
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfileScreen(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<EditProfileViewModel>()
    val userSub by viewModel.userSub.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val oldImageUri by remember { mutableStateOf<Uri?>(Uri.parse(String.format(Constants.USER_IMAGE_URL, userSub))) }
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val isScreenLoading by viewModel.isScreenLoading.collectAsState()

    val schoolName by viewModel.schoolName.collectAsState()
    var isSchoolExpanded by remember { mutableStateOf(false) }
    var isSchoolEnabled by remember { mutableStateOf(true) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { newUri: Uri? ->
        coroutineScope.launch(Dispatchers.Main) {
            newUri?.let {
                imageUri = it
                val bitmap = withContext(Dispatchers.IO) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    }
                }
                val updatedResult = viewModel.updateUser(bitmap)
                if (!updatedResult) {
                    imageUri = null
                }
            }
        }
    }

    viewModel.fetchUser()

    if (isScreenLoading) {
        LoadingScreen()
        return
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        navController.navigateUp()
                    },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUri != null) {
                GlideImage(
                    model = imageUri.toString(),
                    contentDescription = "Sample Image",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            } else if (oldImageUri != null) {
                GlideImage(
                    model = oldImageUri.toString(),
                    contentDescription = "Sample Image",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(128.dp)
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
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(text = "Edit")
            }

            Spacer(modifier = Modifier.size(32.dp))

            BottomsheetField(isExpanded = isSchoolExpanded, isEnabled = isSchoolEnabled, title = "Where I went to school: $schoolName", onExpandedChange = {
                isSchoolExpanded = it
            }) {
                Column {
                    OutlinedTextField(
                        value = schoolName,
                        onValueChange = { viewModel.onSchoolNameChange(it) },
                        label = { Text("Where I went to school") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LoadingButton(
                        onClick = {
                            coroutineScope.launch(Dispatchers.Main) {
                                if (viewModel.updateSchoolName(schoolName)) {
                                    isSchoolExpanded = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = isLoading,
                        buttonText = "Save"
                    )
                }
            }
        }
    }
}