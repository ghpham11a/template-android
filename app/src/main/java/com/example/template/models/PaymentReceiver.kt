package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentReceiver(
    @SerializedName("accountId")
    val accountId: String? = null,
    @SerializedName("email")
    val email: String? = null
): Serializable