package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUserPrivateResponse(
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
    val phoneNumber: String,
    @SerializedName("preferredName")
    val preferredName: String
): Serializable