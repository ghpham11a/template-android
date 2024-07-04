package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreatePayoutMethodResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("accountHolderName")
    val accountHolderName: String,
    @SerializedName("accountHolderType")
    val accountHolderType: String,
    @SerializedName("bankName")
    val bankName: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("last4")
    val last4: String,
    @SerializedName("routingNumber")
    val routingNumber: String,
    @SerializedName("status")
    val status: String
): Serializable