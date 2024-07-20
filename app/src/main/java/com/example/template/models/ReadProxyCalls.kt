package com.example.template.models



import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadProxyCallsResponse(
    @SerializedName("calls")
    val calls: List<ProxyCall>? = null
): Serializable