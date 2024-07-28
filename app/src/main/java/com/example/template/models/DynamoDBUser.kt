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
    var tags: List<Int>? = null
): Serializable