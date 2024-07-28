package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUserPublicResponse(
    @SerializedName("schoolName")
    val schoolName: String,
    @SerializedName("tags")
    var tags: List<Int>? = null
): Serializable