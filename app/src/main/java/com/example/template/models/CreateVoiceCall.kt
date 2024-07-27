package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateVoiceCallRequest(
    @SerializedName("senderId")
    var senderId: String? = null,
    @SerializedName("receiverId")
    var receiverId: String? = null,
): Serializable

data class CreateVoiceCallResponse(
    @SerializedName("message")
    var message: String? = null,
): Serializable