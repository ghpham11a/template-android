package com.example.template.networking

import com.example.template.models.AdminUpdateUserBody
import com.example.template.models.CheckIfUserExistsResponse
import com.example.template.models.ReadUserResponse
import com.example.template.models.UpdateUserBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryName
import java.time.LocalDateTime
import java.time.ZoneId

interface APIGatewayService {
    @GET("admin/users")
    suspend fun adminReadUser(
        @Query("username") username: String? = null,
        @Query("phoneNumber") phoneNumber: String? = null
    ): Response<CheckIfUserExistsResponse>

    @Headers("Content-Type: application/json")
    @PATCH("admin/users/{username}/enable")
    suspend fun adminEnableUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
        @Body body: AdminUpdateUserBody
    ): Response<String>

    @Headers("Content-Type: application/json")
    @PATCH("admin/users/{username}/disable")
    suspend fun adminDisableUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
        @Body body: AdminUpdateUserBody
    ): Response<String>

    @DELETE("admin/users/{username}")
    suspend fun adminDeleteUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
    ): Response<String>

    @PATCH("private/users/{userId}")
    suspend fun updateUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userSub: String,
        @Body body: UpdateUserBody
    ): Response<String>

    @GET("private/users/{userId}")
    suspend fun privateReadUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String
    ): Response<ReadUserResponse>

    @GET("public/users/{userId}")
    suspend fun publicReadUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String
    ): Response<ReadUserResponse>
}

// Create Retrofit instance
object APIGateway {

    fun buildHeaders(): Map<String, String> {
        val offset = LocalDateTime.now().atZone(ZoneId.systemDefault()).offset.toString()
        return mapOf<String, String>(
            "Content-Type" to "application/json",
            "Accept-Encoding" to "gzip,deflate,br",
            "Accept" to "*/*",
            "Platform" to "fcm",
            "Origin-Timezone" to offset
        )
    }

    fun buildAuthorizedHeaders(token: String): Map<String, String> {
        val offset = LocalDateTime.now().atZone(ZoneId.systemDefault()).offset.toString()
        return mapOf<String, String>(
            "Content-Type" to "application/json",
            "Authorization" to "Bearer $token",
            "Accept-Encoding" to "gzip,deflate,br",
            "Accept" to "*/*",
            "Platform" to "fcm",
            "Origin-Timezone" to offset
        )
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://gviivl9o82.execute-api.us-east-1.amazonaws.com/qa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: APIGatewayService by lazy {
        retrofit.create(APIGatewayService::class.java)
    }
}