package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadVoiceCallsResponse(
    @SerializedName("calls")
    val calls: List<VoiceCall>? = null
): Serializable