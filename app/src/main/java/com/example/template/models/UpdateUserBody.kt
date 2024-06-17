package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateUserBody(
    @SerializedName("updateImage")
    val updateImage: UpdateImage? = null,
    @SerializedName("updateLegalName")
    val updateLegalName: UpdateLegalName? = null,
    @SerializedName("updatePreferredName")
    val updatePreferredName: UpdatePreferredName? = null,
    @SerializedName("updatePhoneNumber")
    val updatePhoneNumber: UpdatePhoneNumber? = null,
    @SerializedName("updateSchool")
    val updateSchool: UpdateSchool? = null
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

data class UpdatePreferredName(
    @SerializedName("preferredName")
    val preferredName: String
): Serializable

data class UpdatePhoneNumber(
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("username")
    val username: String
): Serializable

data class UpdateSchool(
    @SerializedName("schoolName")
    val schoolName: String
): Serializable