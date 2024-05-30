package com.example.template.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckIfUserExistsResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String
): Serializable

data class Data(
    @SerializedName("Enabled")
    val enabled: Boolean,
    @SerializedName("ResponseMetadata")
    val responseMetadata: ResponseMetadata,
    @SerializedName("UserAttributes")
    val userAttributes: List<UserAttribute>,
    @SerializedName("UserCreateDate")
    val userCreateDate: String,
    @SerializedName("UserLastModifiedDate")
    val userLastModifiedDate: String,
    @SerializedName("UserStatus")
    val userStatus: String,
    @SerializedName("Username")
    val username: String
): Serializable

data class ResponseMetadata(
    @SerializedName("HTTPHeaders")
    val httpHeaders: HTTPHeaders,
    @SerializedName("HTTPStatusCode")
    val httpStatusCode: Int,
    @SerializedName("RequestId")
    val requestId: String,
    @SerializedName("RetryAttempts")
    val retryAttempts: Int
): Serializable

data class HTTPHeaders(
    @SerializedName("connection")
    val connection: String,
    @SerializedName("content-length")
    val contentLength: String,
    @SerializedName("content-type")
    val contentType: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("x-amzn-requestid")
    val xAmznRequestId: String
): Serializable

data class UserAttribute(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: String
): Serializable