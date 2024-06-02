package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateUserBody(
    @SerializedName("updateImage")
    val updateImage: UpdateImage,
): Serializable


data class UpdateImage(
    @SerializedName("imageData")
    val imageData: String
): Serializable