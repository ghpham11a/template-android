package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadStripeAccountsResponse(
    @SerializedName("accounts")
    val accounts: List<PaymentReceiver>,
): Serializable