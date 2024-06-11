package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateUserBody(
    @SerializedName("updateImage")
    val updateImage: UpdateImage? = null,
    @SerializedName("updateLegalName")
    val updateLegalName: UpdateLegalName? = null,
    @SerializedName("updatePhoneNumber")
    val updatePhoneNumber: UpdatePhoneNumber? = null
): Serializable


data class UpdateImage(
    @SerializedName("imageData")
    val imageData: String
): Serializable

data class UpdateLegalName(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String
): Serializable

data class UpdatePhoneNumber(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("username")
    val username: String
): Serializable