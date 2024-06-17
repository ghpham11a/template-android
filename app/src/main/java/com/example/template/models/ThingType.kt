package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ThingType(
    @SerializedName("group")
    val group: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
): Serializable