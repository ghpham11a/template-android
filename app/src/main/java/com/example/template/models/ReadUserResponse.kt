package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUserResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
): Serializable