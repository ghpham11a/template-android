package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeletePaymentMethodRequest(
    @SerializedName("accountId")
    val accountId: String? = null,
    @SerializedName("paymentMethodId")
    val paymentMethodId: String? = null
): Serializable

data class DeletePaymentMethodResponse(
    @SerializedName("message")
    val message: String? = null
): Serializable