package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateAZCSAccessTokenResponse(
    @SerializedName("token")
    var token: String? = null,
    @SerializedName("identity")
    var identity: String? = null,
    @SerializedName("expiresOn")
    var expiresOn: String? = null
): Serializable