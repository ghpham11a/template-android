package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReadUsersResponse(
    @SerializedName("users")
    val users: List<DynamoDBUser>? = null
): Serializable