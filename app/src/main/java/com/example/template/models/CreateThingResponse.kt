package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateThingResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("error")
    val error: String,
): Serializable