package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Tag(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("title")
    val title: String? = null
): Serializable
