package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Chat(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("senderId")
    val senderId: String? = null,
    @SerializedName("receiverId")
    val receiverId: String? = null,
): Serializable