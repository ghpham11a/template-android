package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateSetupIntentRequest(
    @SerializedName("stripeCustomerId")
    var stripeCustomerId: String? = null
): Serializable