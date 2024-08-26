package com.example.template.ui.screens.videocallhub

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
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
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.azure.android.communication.calling.AcceptCallOptions
import com.azure.android.communication.calling.CallEndReason
import com.azure.android.communication.calling.CallState
import com.azure.android.communication.calling.CreateViewOptions
import com.azure.android.communication.calling.IncomingCall
import com.azure.android.communication.calling.LocalVideoStream
import com.azure.android.communication.calling.ParticipantsUpdatedEvent
import com.azure.android.communication.calling.ParticipantsUpdatedListener
import com.azure.android.communication.calling.PropertyChangedListener
import com.azure.android.communication.calling.RemoteParticipant
import com.azure.android.communication.calling.RemoteVideoStream
import com.azure.android.communication.calling.RemoteVideoStreamsEvent
import com.azure.android.communication.calling.RendererListener
import com.azure.android.communication.calling.ScalingMode
import com.azure.android.communication.calling.StartCallOptions
import com.azure.android.communication.calling.VideoDeviceInfo
import com.azure.android.communication.calling.VideoOptions
import com.azure.android.communication.calling.VideoStreamRenderer
import com.azure.android.communication.calling.VideoStreamRendererView
import com.azure.android.communication.common.CommunicationIdentifier
import com.azure.android.communication.common.CommunicationTokenCredential
import com.azure.android.communication.common.CommunicationUserIdentifier
import com.azure.android.communication.common.MicrosoftTeamsUserIdentifier
import com.azure.android.communication.common.PhoneNumberIdentifier
import com.azure.android.communication.common.UnknownIdentifier
import com.example.template.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException


internal class StreamData(
    var stream: RemoteVideoStream? = null,
    var renderer: VideoStreamRenderer? = null,
    var rendererView: VideoStreamRendererView? = null
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VideoCallScreen(navController: NavController, id: String) {

    val viewModel = hiltViewModel<VideoCallViewModel>()
    val couroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle the permissions result here
    }

    // AZCS
    // var callAgent: CallAgent? = null
    var currentCamera: VideoDeviceInfo? = null
    var currentVideoStream: LocalVideoStream? = null
    // var deviceManager: DeviceManager? = null
    var incomingCall: IncomingCall? = null
    // var call: Call? = null
    var previewRenderer: VideoStreamRenderer? = null
    var preview: VideoStreamRendererView? = null
    val streamData: MutableMap<Int, StreamData> = mutableMapOf()
    val renderRemoteVideo = true
    var remoteParticipantUpdatedListener: ParticipantsUpdatedListener
    var onStateChangedListener: PropertyChangedListener

    val joinedParticipants = HashSet<String>()

    var callButton: Button? = null
    var localVideoContainer: LinearLayout? = null
    var switchSourceButton: Button? = null
    var remoteVideoContainer: GridLayout? = null
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

        if (viewModel.callAgent != null) {
            Toast.makeText(context, "Call agent exists", Toast.LENGTH_LONG).show()
            return
        }

        val userToken = viewModel.getAccessToken(id) ?: return
        Log.d("__DEBUG", "userToken ${userToken}")
        try {
            val credential = CommunicationTokenCredential(userToken)
            viewModel.callAgent = viewModel.callClient.createCallAgent(context, credential).get()
            Toast.makeText(context, "Call agent created", Toast.LENGTH_LONG).show()

        } catch (ex: Exception) {
            Toast.makeText(context, "Failed to create call agent.", Toast.LENGTH_LONG).show()
        }
    }

    fun getNextAvailableCamera(camera: VideoDeviceInfo?): VideoDeviceInfo? {
        val cameras = viewModel.deviceManager?.cameras ?: emptyList()
        var currentIndex = 0
        if (camera == null) {
            return if (cameras.isEmpty()) null else cameras[0]
        }

        for (i in cameras.indices) {
            if (camera.id == cameras[i].id) {
                currentIndex = i
                break
            }
        }
        val newIndex = (currentIndex + 1) % cameras.size
        return cameras[newIndex]
    }

    fun showPreview(stream: LocalVideoStream) {
        previewRenderer = VideoStreamRenderer(stream, context)
        preview = previewRenderer?.createView(CreateViewOptions(ScalingMode.FIT))
        preview?.tag = 0
        runOnUiThread {
            localVideoContainer?.addView(preview)
            switchSourceButton?.visibility = View.VISIBLE
        }
    }

    fun setDeviceManager() {
        try {
            viewModel.deviceManager = viewModel.callClient.getDeviceManager(context).get()
            Toast.makeText(context, "Device manager set", Toast.LENGTH_SHORT).show()

            //
            val acceptCallOptions = AcceptCallOptions()
            val cameras = viewModel.deviceManager?.cameras
            if (cameras?.isNotEmpty() == true) {
                currentCamera = getNextAvailableCamera(null)
                currentVideoStream = LocalVideoStream(currentCamera, context)
                val videoStreams = arrayOf(currentVideoStream)
                val videoOptions = VideoOptions(videoStreams)
                acceptCallOptions.videoOptions = videoOptions
                currentVideoStream?.let {
                    showPreview(it)
                }
            }
            //////////////

        } catch (ex: java.lang.Exception) {
            Toast.makeText(context, "Failed to set device manager.", Toast.LENGTH_SHORT).show()
        }
    }


    fun switchSource() {
        if (currentVideoStream != null) {
            try {
                currentCamera = currentCamera?.let { getNextAvailableCamera(it) };
                currentVideoStream?.switchSource(currentCamera)?.get();
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    fun startRenderingVideo(data: StreamData) {
        if (data.renderer != null) {
            return
        }
        data.renderer = VideoStreamRenderer(data.stream, context)
        data.renderer?.addRendererListener(object : RendererListener {
            override fun onFirstFrameRendered() {
                val text = data.renderer?.size.toString()
                Log.i("VideoCallScreen", "Video rendering at: $text")
            }

            override fun onRendererFailedToStart() {
                val text = "Video failed to render"
                Log.i("VideoCallScreen", text)
            }
        })
        data.rendererView = data.renderer?.createView(CreateViewOptions(ScalingMode.FIT))
        data.rendererView?.tag = data.stream?.id
        runOnUiThread {
            val params = GridLayout.LayoutParams(remoteVideoContainer?.layoutParams)
            val displayMetrics = DisplayMetrics()

            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            params.height = (displayMetrics.heightPixels / 2.5).toInt()
            params.width = displayMetrics.widthPixels / 2
            remoteVideoContainer?.addView(data.rendererView, params)
        }
    }

    fun stopRenderingVideo(stream: RemoteVideoStream) {
        val data = streamData[stream.id] ?: return
        if (data.renderer == null) {
            return
        }
        runOnUiThread {
            for (i in 0 until (remoteVideoContainer?.childCount ?: 0)) {
                val childView = remoteVideoContainer?.getChildAt(i)
                if (childView?.tag == data.stream?.id) {
                    remoteVideoContainer?.removeViewAt(i)
                }
            }
        }
        data.rendererView = null
        // Dispose renderer
        data.renderer?.dispose()
        data.renderer = null
    }

    fun videoStreamsUpdated(videoStreamsEventArgs: RemoteVideoStreamsEvent) {
        for (stream in videoStreamsEventArgs.addedRemoteVideoStreams) {
            val data = StreamData(stream, null, null)
            streamData[stream.id] = data
            if (renderRemoteVideo) {
                startRenderingVideo(data)
            }
        }

        for (stream in videoStreamsEventArgs.removedRemoteVideoStreams) {
            stopRenderingVideo(stream)
        }
    }

    fun getId(remoteParticipant: RemoteParticipant): String {
        val identifier = remoteParticipant.identifier
        return when (identifier) {
            is PhoneNumberIdentifier -> identifier.phoneNumber
            is MicrosoftTeamsUserIdentifier -> identifier.userId
            is CommunicationUserIdentifier -> identifier.id
            else -> (identifier as UnknownIdentifier).id
        }
    }

    fun handleAddedParticipants(participants: MutableList<RemoteParticipant>?) {
        println("h")
        for (remoteParticipant in participants ?: emptyList<RemoteParticipant>()) {
            if (!joinedParticipants.contains(getId(remoteParticipant))) {
                joinedParticipants.add(getId(remoteParticipant))

                if (renderRemoteVideo) {
                    for (stream in remoteParticipant.videoStreams) {
                        val data = StreamData(stream, null, null)
                        streamData[stream.id] = data
                        startRenderingVideo(data)
                    }
                }
                remoteParticipant.addOnVideoStreamsUpdatedListener { videoStreamsEventArgs ->
                    videoStreamsUpdated(videoStreamsEventArgs)
                }
            }
        }
    }

    fun handleCallState() {
        handleAddedParticipants(viewModel.call?.getRemoteParticipants());
    }

    fun answerIncomingCall() {

        if (incomingCall == null) {
            return
        }
        val acceptCallOptions = AcceptCallOptions()
        val cameras = viewModel.deviceManager?.cameras
        if (cameras?.isNotEmpty() == true) {
            currentCamera = getNextAvailableCamera(null)
            currentVideoStream = LocalVideoStream(currentCamera, context)
            val videoStreams = arrayOf(currentVideoStream)
            val videoOptions = VideoOptions(videoStreams)
            acceptCallOptions.videoOptions = videoOptions
            currentVideoStream?.let {
                showPreview(it)
            }
        }

        try {
            viewModel.call = incomingCall!!.accept(context, acceptCallOptions).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        viewModel.call?.addOnRemoteParticipantsUpdatedListener {
            handleAddedParticipants(it.addedParticipants)
        }
        viewModel.call?.addOnStateChangedListener {
            when (viewModel.call?.state) {
                CallState.CONNECTED -> {
                    runOnUiThread {
                        Toast.makeText(context, "Call is CONNECTED", Toast.LENGTH_SHORT).show()
                    }
                    handleCallState()
                }
                CallState.DISCONNECTED -> {
                    val pussy = viewModel.call
                    val message: CallEndReason? = pussy?.callEndReason
                    println("Hello World")
                    runOnUiThread {
                        Toast.makeText(context, "Call is DISCONNECTED answerIncomingCall2", Toast.LENGTH_SHORT).show()
                    }
                    // previewRenderer?.dispose()
                    switchSourceButton?.visibility = View.INVISIBLE
                }
                else -> {}
            }
        }
    }

    fun handleIncomingCall() {
        viewModel.callAgent?.addOnIncomingCallListener {
            incomingCall = it
            couroutineScope.launch {
                answerIncomingCall()
            }
        }
    }

    fun handleRemoteParticipantsUpdate(args: ParticipantsUpdatedEvent) {
        handleAddedParticipants(args.addedParticipants)
    }

    fun startCall() {

        val callId = viewModel.getIdentity(id) ?: return

        val participants = ArrayList<CommunicationIdentifier>()
        val cameras = viewModel.deviceManager?.cameras

        val options = StartCallOptions()
        if (cameras != null) {
            if (cameras.isNotEmpty()) {
                currentCamera = getNextAvailableCamera(null)
                currentVideoStream = LocalVideoStream(currentCamera, context)
                val videoStreams = arrayOf(currentVideoStream)
                val videoOptions = VideoOptions(videoStreams)
                options.videoOptions = videoOptions
                currentVideoStream?.let {
                    showPreview(it)
                }
            }
        }

        val otherId = viewModel.getIdentityReversed(id) ?: return
        Log.d("__DEBUG", "callId ${callId}")
        participants.add(CommunicationUserIdentifier(callId))
        // participants.add(CommunicationUserIdentifier(otherId))

        viewModel.call = viewModel.callAgent?.startCall(
            context,
            participants,
            options
        ) ?: run {
            Log.d("__DEBUG", "callAgent is null")
            return
        }

        viewModel.call?.addOnRemoteParticipantsUpdatedListener {
            handleAddedParticipants(it.addedParticipants)
        }

        viewModel.call?.addOnStateChangedListener {
            when (viewModel.call?.state) {
                CallState.CONNECTED -> {
                    runOnUiThread {
                        Toast.makeText(context, "Call is CONNECTED", Toast.LENGTH_SHORT).show()
                    }
                    handleCallState()
                }
                CallState.DISCONNECTED -> {
                    val pussy = viewModel.call
                    val message = pussy?.callEndReason?.code
                    println("Hello World")
                    runOnUiThread {
                        Toast.makeText(context, "Call is DISCONNECTED startCall", Toast.LENGTH_SHORT).show()
                    }
                    previewRenderer?.dispose()
                    switchSourceButton?.visibility = View.INVISIBLE
                }
                else -> {

                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(1000)
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
                    callButton = view.findViewById(R.id.call_button)
                    callButton?.setOnClickListener {
                        couroutineScope.launch {
                            // callAgent?.startCall()
                            startCall()
                        }
                    }

                    localVideoContainer = view.findViewById(R.id.localvideocontainer)
                    switchSourceButton = view.findViewById(R.id.switch_source)
                    remoteVideoContainer = view.findViewById(R.id.remotevideocontainer)

                }
            )
        }
    }
}