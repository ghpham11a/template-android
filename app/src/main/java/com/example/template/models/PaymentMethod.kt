package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentMethod(
    @SerializedName("last4")
    var last4: String? = null,
    @SerializedName("brand")
    var brand: String? = null,
    @SerializedName("expiry")
    var expiry: String? = null,
    //
    var isSelected: Boolean? = null
): Serializable