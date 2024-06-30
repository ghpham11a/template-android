package com.example.template.networking

import com.example.template.models.AdminDeleteUserResponse
import com.example.template.models.AdminDisableUserResponse
import com.example.template.models.AdminEnableUserResponse
import com.example.template.models.AdminUpdateUserBody
import com.example.template.models.CheckIfUserExistsResponse
import com.example.template.models.CreateSetupIntentRequest
import com.example.template.models.CreateSetupIntentResponse
import com.example.template.models.CreateThingResponse
import com.example.template.models.ReadPaymentMethodsResponse
import com.example.template.models.ReadPayoutMethodsResponse
import com.example.template.models.ReadUserPrivateResponse
import com.example.template.models.ReadUserPublicResponse
import com.example.template.models.Thing
import com.example.template.models.UpdateUserBody
import com.example.template.models.UpdateUserPrivateResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
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
    ): Response<AdminEnableUserResponse>

    @Headers("Content-Type: application/json")
    @PATCH("admin/users/{username}/disable")
    suspend fun adminDisableUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
        @Body body: AdminUpdateUserBody
    ): Response<AdminDisableUserResponse>

    @DELETE("admin/users/{username}")
    suspend fun adminDeleteUser(
        @HeaderMap headers: Map<String, String>,
        @Path("username") username: String,
    ): Response<AdminDeleteUserResponse>

    @PATCH("private/users/{userId}")
    suspend fun privateUpdateUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userSub: String,
        @Body body: UpdateUserBody
    ): Response<UpdateUserPrivateResponse>

    @GET("private/users/{userId}")
    suspend fun privateReadUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String
    ): Response<ReadUserPrivateResponse>

    @GET("public/users/{userId}")
    suspend fun publicReadUser(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String
    ): Response<ReadUserPublicResponse>

    @POST("private/things")
    suspend fun privateCreateThing(
        @HeaderMap headers: Map<String, String>,
        @Body body: Thing
    ): Response<CreateThingResponse>

    @POST("private/users/{userId}/payments")
    suspend fun privateCreatePaymentIntent(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
        @Body body: CreateSetupIntentRequest
    ): Response<CreateSetupIntentResponse>

    @GET("private/users/{userId}/payments")
    suspend fun privateReadPaymentMethods(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
        @Query("customerId") customerId: String
    ): Response<ReadPaymentMethodsResponse>

    @GET("private/users/{userId}/payouts")
    suspend fun privateReadPayoutMethods(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
        @Query("accountId") accountId: String
    ): Response<ReadPayoutMethodsResponse>
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