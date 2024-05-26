package com.example.template.models

import java.lang.Exception

data class AWSMobileClientResponse<T>(
    val isSuccessful: Boolean,
    val result: T? = null,
    val exception: Exception? = null
)
