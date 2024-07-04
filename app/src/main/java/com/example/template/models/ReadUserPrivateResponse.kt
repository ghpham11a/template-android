package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUserPrivateResponse(
    @SerializedName("user")
    val user: DynamoDBUser? = null,
    @SerializedName("stripeAccount")
    val stripeAccount: StripeAccount? = null,
): Serializable