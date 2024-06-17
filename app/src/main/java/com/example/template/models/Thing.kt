package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Thing(
    @SerializedName("thingType")
    val thingType: ThingType? = null,
    @SerializedName("thingDescription")
    val thingDescription: String? = null
): Serializable