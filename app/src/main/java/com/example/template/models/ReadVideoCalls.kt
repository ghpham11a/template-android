package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadVideoCallsResponse(
    @SerializedName("calls")
    val calls: List<VideoCall>? = null
): Serializable