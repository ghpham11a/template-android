package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PayoutMethod(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("accountHolderName")
    val accountHolderName: String? = null,
    @SerializedName("accountHolderType")
    var accountHolderType: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("last4")
    var last4: String? = null,
    @SerializedName("status")
    var status: String? = null,
    //
    var isSelected: Boolean? = null
): Serializable
