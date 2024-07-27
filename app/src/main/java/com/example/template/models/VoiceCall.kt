package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VoiceCall(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("senderId")
    val senderId: String? = null,
    @SerializedName("senderIdentity")
    val senderIdentity: String? = null,
    @SerializedName("senderToken")
    val senderToken: String? = null,
    @SerializedName("senderTokenExpiresOn")
    val senderTokenExpiresOn: String? = null,
    @SerializedName("receiverId")
    val receiverId: String? = null,
    @SerializedName("receiverIdentity")
    val receiverIdentity: String? = null,
    @SerializedName("receiverToken")
    val receiverToken: String? = null,
    @SerializedName("receiverTokenExpiresOn")
    val receiverTokenExpiresOn: String? = null,
): Serializable