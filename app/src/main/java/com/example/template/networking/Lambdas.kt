package com.example.template.networking

import com.example.template.models.AdminUpdateUserBody
import com.example.template.models.CheckIfUserExistsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.ZoneId

interface LambdaService {
    @GET("admin/users")
    suspend fun adminGetUser(
        @Query("username") username: String
    ): Response<CheckIfUserExistsResponse>

    @Headers("Content-Type: application/json")
    @PUT("admin/users")
    suspend fun adminUpdateUser(
        @HeaderMap headers: Map<String, String>,
        @Body body: AdminUpdateUserBody
    ): Response<String>

    @DELETE("admin/users")
    suspend fun adminDeleteUser(
        @Query("username") username: String
    ): Response<String>
}

// Create Retrofit instance
object Lambdas {

    fun getHeaders(token: String): Map<String, String> {
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