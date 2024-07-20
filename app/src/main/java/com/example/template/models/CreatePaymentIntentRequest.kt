package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatePaymentIntentRequest(
    @SerializedName("customerId")
    var customerId: String? = null,
    @SerializedName("accountId")
    var accountId: String? = null,
    @SerializedName("amount")
    var amount: String? = null,
): Serializable