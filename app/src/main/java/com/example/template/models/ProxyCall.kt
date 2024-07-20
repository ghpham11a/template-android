package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProxyCall(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("senderId")
    val senderId: String? = null,
    @SerializedName("receiverId")
    val receiverId: String? = null,
    @SerializedName("senderProxy")
    val senderProxy: String? = null,
    @SerializedName("receiverProxy")
    val receiverProxy: String? = null,
): Serializable
