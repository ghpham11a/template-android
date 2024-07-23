package com.example.template.ui.screens.videocallhub

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.azure.android.communication.calling.Call
import com.azure.android.communication.calling.CallAgent
import com.azure.android.communication.calling.CallClient
import com.azure.android.communication.calling.DeviceManager
import com.azure.android.communication.calling.IncomingCall
import com.azure.android.communication.calling.LocalVideoStream
import com.azure.android.communication.calling.ParticipantsUpdatedListener
import com.azure.android.communication.calling.PropertyChangedListener
import com.azure.android.communication.calling.RemoteVideoStream
import com.azure.android.communication.calling.VideoDeviceInfo
import com.azure.android.communication.calling.VideoStreamRenderer
import com.azure.android.communication.calling.VideoStreamRendererView
import com.azure.android.communication.common.CommunicationTokenCredential
import com.example.template.R
import kotlinx.coroutines.launch


internal class StreamData(
    var stream: RemoteVideoStream,
    var renderer: VideoStreamRenderer,
    var rendererView: VideoStreamRendererView
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VideoCallScreen(navController: NavController, callee: String) {

    val viewModel = hiltViewModel<VideoCallViewModel>()
    val couroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle the permissions result here
    }

    // AZCS
    var callAgent: CallAgent? = null
    var currentCamera: VideoDeviceInfo
    var currentVideoStream: LocalVideoStream
    var deviceManager: DeviceManager
    var incomingCall: IncomingCall
    var call: Call
    var previewRenderer: VideoStreamRenderer
    var preview: VideoStreamRendererView
    val streamData: Map<Int, StreamData> = HashMap<Int, StreamData>()
    val renderRemoteVideo = true
    var remoteParticipantUpdatedListener: ParticipantsUpdatedListener
    var onStateChangedListener: PropertyChangedListener

    val joinedParticipants = HashSet<String>()
    // End AZCS

    fun getAllPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
        val permissionsToAskFor = ArrayList<String>()
        for (permission in requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAskFor.add(permission)
            }
        }
        if (permissionsToAskFor.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToAskFor.toTypedArray())
        }
    }

    fun createAgent() {
        val userToken = ""
        try {
            val credential = CommunicationTokenCredential(userToken)
            val callClient = CallClient()
            callAgent = callClient.createCallAgent(context, credential).get()
        } catch (ex: Exception) {
            Toast.makeText(context, "Failed to create call agent.", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleIncomingCall() {
        callAgent?.addOnIncomingCallListener {
            incomingCall = it
            couroutineScope.launch {
                // answerIncomingCall()
            }
        }
    }


    fun setDeviceManager() {
        try {
            val callClient = CallClient()
            deviceManager = callClient.getDeviceManager(context).get()
        } catch (ex: java.lang.Exception) {
            Toast.makeText(context, "Failed to set device manager.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        getAllPermissions()
        createAgent()
        setDeviceManager()
    }

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
                    LayoutInflater.from(context).inflate(R.layout.video_call_screen, null, false)
                },
                update = { view ->
//                    val button = view.findViewById<Button>(R.id.button_make_api_call)
//                    val text = view.findViewById<TextView>(R.id.text_api_response)

//                    text.text = textContent
//
//                    button.setOnClickListener {
//                        // Handle button click here
//                        couroutineScope.launch {
//                            viewModel.makeAPICall()
//                        }
//                    }
                }
            )
        }
    }
}