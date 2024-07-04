package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class DynamoDBUser(
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("preferredName")
    val preferredName: String? = null,
    @SerializedName("stripeCustomerId")
    val stripeCustomerId: String? = null,
    @SerializedName("stripeAccountId")
    val stripeAccountId: String? = null
): Serializable