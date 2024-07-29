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
    val updateSchool: UpdateSchool? = null,
    @SerializedName("updateEmail")
    val updateEmail: UpdateEmail? = null,
    @SerializedName("updateAvailability")
    val updateAvailability: UpdateAvailability? = null,
    @SerializedName("updateTags")
    val updateTags: UpdateTags? = null
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

data class UpdateEmail(
    @SerializedName("email")
    val email: String
): Serializable

data class UpdateSchool(
    @SerializedName("schoolName")
    val schoolName: String
): Serializable

data class UpdateAvailability(
    @SerializedName("availabilityType1")
    var availabilityType1: List<String>? = null,
    @SerializedName("availabilityType2")
    var availabilityType2: List<String>? = null,
    @SerializedName("availabilityType3")
    var availabilityType3: List<String>? = null,
    @SerializedName("availabilityType4")
    var availabilityType4: List<String>? = null,
): Serializable

data class UpdateTags(
    @SerializedName("tags")
    val tags: List<Int>? = null
): Serializable
