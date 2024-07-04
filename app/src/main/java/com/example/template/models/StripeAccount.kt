package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StripeAccount(
    @SerializedName("payoutsEnabled")
    val payoutsEnabled: Boolean = false,
    @SerializedName("disabledReason")
    val disabledReason: String? = null,
    @SerializedName("accountLinkUrl")
    val accountLinkUrl: String? = null
): Serializable

