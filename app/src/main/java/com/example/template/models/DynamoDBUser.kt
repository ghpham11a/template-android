package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class DynamoDBUser(
    @SerializedName("userId")
    var userId: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("firstName")
    var firstName: String? = null,
    @SerializedName("lastName")
    var lastName: String? = null,
    @SerializedName("preferredName")
    var preferredName: String? = null,
    @SerializedName("countryCode")
    var countryCode: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("stripeCustomerId")
    var stripeCustomerId: String? = null,
    @SerializedName("stripeAccountId")
    var stripeAccountId: String? = null,
    @SerializedName("tags")
    var tags: List<Int>? = null,
    @SerializedName("availabilityType1")
    var availabilityType1: List<String>? = null,
    @SerializedName("availabilityType2")
    var availabilityType2: List<String>? = null,
    @SerializedName("availabilityType3")
    var availabilityType3: List<String>? = null,
    @SerializedName("availabilityType4")
    var availabilityType4: List<String>? = null,
): Serializable