package com.example.template.models

data class VideoCallEvent(
    var user: DynamoDBUser? = null,
    var videoCall: VideoCall? = null
)
