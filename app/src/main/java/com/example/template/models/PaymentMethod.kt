package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentMethod(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("brand")
    var brand: String? = null,
    @SerializedName("last4")
    var last4: String? = null,
    @SerializedName("expMonth")
    var expMonth: String? = null,
    @SerializedName("expYear")
    var expYear: String? = null,
    //
    var isSelected: Boolean? = null
): Serializable