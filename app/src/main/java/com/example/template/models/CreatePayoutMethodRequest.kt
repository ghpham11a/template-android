package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatePayoutMethodRequest(
    @SerializedName("route")
    var route: String? = null,

    // User stuff
    @SerializedName("customerId")
    var customerId: String? = null,
    @SerializedName("accountId")
    var accountId: String? = null,

    // Stripe stuff
    @SerializedName("accountHolderName")
    var accountHolderName: String? = null,
    @SerializedName("accountNumber")
    var accountNumber: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("routingNumber")
    var routingNumber: String? = null
): Serializable
