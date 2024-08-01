package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUserPublicResponse(
    @SerializedName("schoolName")
    val schoolName: String,
    @SerializedName("tags")
    var tags: List<Int>? = null,
    @SerializedName("availabilityType1")
    var availabilityType1: List<String>? = null,
    @SerializedName("availabilityType2")
    var availabilityType2: List<String>? = null,
    @SerializedName("availabilityType3")
    var availabilityType3: List<String>? = null,
    @SerializedName("availabilityType4")
    var availabilityType4: List<String>? = null,
): Serializable