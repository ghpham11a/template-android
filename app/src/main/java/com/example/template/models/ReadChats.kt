package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadChatsResponse(
    @SerializedName("calls")
    val chats: List<Chat>? = null
): Serializable