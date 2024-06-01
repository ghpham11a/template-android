package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AdminUpdateUserBody(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("username")
    val username: String? = null,
): Serializable