package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUserPrivateResponse(
    @SerializedName("user")
    var user: DynamoDBUser? = null,
    @SerializedName("stripeAccount")
    var stripeAccount: StripeAccount? = null,
): Serializable