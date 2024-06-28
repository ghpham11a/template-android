package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PayoutMethod(
    @SerializedName("accountHolderName")
    val accountHolderName: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("bankName")
    var bankName: String? = null,
    @SerializedName("last4")
    var last4: String? = null,
    @SerializedName("status")
    var status: String? = null,
    //
    var isSelected: Boolean? = null
): Serializable
