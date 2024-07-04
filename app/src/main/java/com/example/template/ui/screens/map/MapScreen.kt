package com.example.template.ui.screens.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.template.BuildConfig
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, FlowPreview::class)
@Composable
fun MapScreen(navController: NavController) {

    val viewModel = hiltViewModel<MapsViewModel>()

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val mapOrigin by viewModel.mapOrigin.collectAsState()
    val marker by viewModel.marker.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(1.35, 103.87), 0f, 0f, 0f)
    }
    val coroutineScope = rememberCoroutineScope()

    var searchPlaceholder by remember { mutableStateOf("Search locations") }
    var searchContent by remember { mutableStateOf("") }
    val searchFlow = remember { MutableSharedFlow<String>() }
    var isTextFieldClicked by remember { mutableStateOf(false) }

    val locations by viewModel.location.collectAsState()

    LaunchedEffect(Unit) {
        if (locationPermissionState.status == PermissionStatus.Granted) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val userLocation = LatLng(location.latitude, location.longitude)
                        viewModel.updateMapOrigin(userLocation)

                    }
                }
            }
        }
    }

    LaunchedEffect(mapOrigin) {
        cameraPositionState.position = CameraPosition(mapOrigin, 15f, 0f, 0f)
    }

    LaunchedEffect(searchFlow) {
        searchFlow
            .debounce(300) // 300ms debounce time
            .collect { query ->
                viewModel.searchLocations(cameraPositionState.position.target, query)
            }
    }

    Scaffold(
        topBar = {
//            TextButton(
//                onClick = {
//                    navController.navigateUp()
//                },
//            ) {
//                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
//            }
        }
    ) {

        if (locationPermissionState.status.isGranted) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {

                    Row {
                        TextButton(
                            onClick = {
                                if (isTextFieldClicked) {
                                    if (marker != null) {
                                        searchContent = searchPlaceholder
                                    } else {
                                        searchContent = ""
                                        searchPlaceholder = "Search location"
                                        viewModel.clearLocations()
                                    }
                                    isTextFieldClicked = false
                                } else {
                                    navController.navigateUp()
                                }
                            },
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(10.dp)
                    ) {

                        if (isTextFieldClicked) {
                            Box(modifier = Modifier.fillMaxWidth()) {

                                TextField(
                                    value = searchContent,
                                    modifier = Modifier.fillMaxWidth(),
                                    onValueChange = {
                                        coroutineScope.launch {
                                            searchFlow.emit(it)
                                        }
                                        searchContent = it
                                    },
                                    singleLine = true,
                                    enabled = true
                                )
                                IconButton(
                                    onClick = {
                                        searchContent = ""
                                        searchPlaceholder = "Search locations"
                                        viewModel.clearLocations()
                                    },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "Add Icon")
                                }
                            }
                        } else {
                            TextField(
                                value = searchPlaceholder,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        isTextFieldClicked = !isTextFieldClicked
                                    },
                                onValueChange = {

                                },
                                enabled = false
                            )
                        }
                    }

                    if (isTextFieldClicked) {
                        LazyColumn(modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)) {
                            items(locations) { prediction ->
                                HorizontalDivider()
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Button(
                                        onClick = {
                                            val selectedPrediction = prediction.getFullText(StyleSpan(Typeface.BOLD)).toString()
                                            searchContent = selectedPrediction
                                            searchPlaceholder = selectedPrediction
                                            isTextFieldClicked = false
                                            viewModel.readPlaceDetails(BuildConfig.MAPS_API_KEY, prediction.placeId)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    ) {
                                        Text(
                                            prediction.getFullText(StyleSpan(Typeface.BOLD)).toString(),
                                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                    ) {
                        marker?.let {
                            Marker(state = MarkerState(position = it))
                        }
                    }
                }
            }
        } else {
            Text("Location permission is required to display the map. ${locationPermissionState.permission}")
        }
    }
}