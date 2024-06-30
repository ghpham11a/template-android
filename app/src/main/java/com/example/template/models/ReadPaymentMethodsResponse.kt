package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadPaymentMethodsResponse(
    @SerializedName("paymentMethods")
    val paymentMethods: List<PaymentMethod>? = null,
): Serializable
