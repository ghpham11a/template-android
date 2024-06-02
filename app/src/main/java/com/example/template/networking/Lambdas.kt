package com.example.template.networking

import com.example.template.models.AdminUpdateUserBody
import com.example.template.models.CheckIfUserExistsResponse
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
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDateTime
import java.time.ZoneId

interface LambdaService {
    @GET("admin/users/{username}")
    suspend fun adminGetUser(
        @Path("username") username: String
    ): Response<CheckIfUserExistsResponse>

    @Headers("Content-Type: application/json")
    @PUT("admin/users/{username}/enable")
    suspend fun adminEnableUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
        @Body body: AdminUpdateUserBody
    ): Response<String>

    @Headers("Content-Type: application/json")
    @PUT("admin/users/{username}")
    suspend fun adminUpdateUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
        @Body body: AdminUpdateUserBody
    ): Response<String>

    @DELETE("admin/users/{username}")
    suspend fun adminDeleteUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
    ): Response<String>

    @PATCH("users/{userSub}")
    suspend fun updateUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userSub") userSub: String,
        @Body body: UpdateUserBody
    ): Response<String>
}

// Create Retrofit instance
object Lambdas {

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
            .baseUrl("https://o7hniu19pc.execute-api.us-east-1.amazonaws.com/qa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LambdaService by lazy {
        retrofit.create(LambdaService::class.java)
    }
}