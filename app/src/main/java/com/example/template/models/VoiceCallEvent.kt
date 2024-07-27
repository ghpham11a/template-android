package com.example.template.models

data class VoiceCallEvent(
    var user: DynamoDBUser? = null,
    var voiceCall: VoiceCall? = null
)
