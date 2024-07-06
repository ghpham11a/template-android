package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeletePayoutMethodRequest(
    @SerializedName("accountId")
    val accountId: String? = null,
    @SerializedName("payoutMethodId")
    val payoutMethodId: String? = null
): Serializable

data class DeletePayoutMethodResponse(
    @SerializedName("message")
    val message: String? = null
): Serializable