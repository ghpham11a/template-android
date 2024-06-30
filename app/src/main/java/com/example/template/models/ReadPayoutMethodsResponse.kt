package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadPayoutMethodsResponse(
    @SerializedName("payoutMethods")
    val payoutMethods: List<PayoutMethod>? = null,
): Serializable
