package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateVideoCallRequest(
    @SerializedName("senderId")
    var senderId: String? = null,
    @SerializedName("receiverId")
    var receiverId: String? = null,
): Serializable

data class CreateVideoCallResponse(
    @SerializedName("message")
    var message: String? = null,
): Serializable