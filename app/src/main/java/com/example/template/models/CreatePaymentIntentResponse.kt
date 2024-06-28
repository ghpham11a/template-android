package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatePaymentIntentResponse(
    @SerializedName("isSuccessful")
    var isSuccessful: Boolean? = null,
    @SerializedName("customerId")
    var customerId: String? = null,
    @SerializedName("ephemeralKey")
    var ephemeralKey: String? = null,
    @SerializedName("setupIntent")
    var setupIntent: String? = null,
    @SerializedName("publishableKey")
    var publishableKey: String? = null,
): Serializable