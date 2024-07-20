package com.example.template.networking

import com.example.template.models.AdminDeleteUserResponse
import com.example.template.models.AdminDisableUserResponse
import com.example.template.models.AdminEnableUserResponse
import com.example.template.models.AdminUpdateUserBody
import com.example.template.models.CheckIfUserExistsResponse
import com.example.template.models.CreateAZCSAccessTokenResponse
import com.example.template.models.CreatePaymentIntentRequest
import com.example.template.models.CreatePayoutMethodRequest
import com.example.template.models.CreatePayoutMethodResponse
import com.example.template.models.CreateProxyCallRequest
import com.example.template.models.CreateProxyCallResponse
import com.example.template.models.CreateSetupIntentRequest
import com.example.template.models.CreateSetupIntentResponse
import com.example.template.models.CreateThingResponse
import com.example.template.models.DeletePaymentMethodRequest
import com.example.template.models.DeletePaymentMethodResponse
import com.example.template.models.DeletePayoutMethodRequest
import com.example.template.models.DeletePayoutMethodResponse
import com.example.template.models.ReadPaymentMethodsResponse
import com.example.template.models.ReadPayoutMethodsResponse
import com.example.template.models.ReadProxyCallsResponse
import com.example.template.models.ReadUserPrivateResponse
import com.example.template.models.ReadUserPublicResponse
import com.example.template.models.ReadUsersResponse
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

    @POST("stripe/payment-intents")
    suspend fun stripeCreatePaymentIntent(
        @HeaderMap headers: Map<String, String>,
        @Body body: CreatePaymentIntentRequest
    ): Response<CreateThingResponse>

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
    suspend fun privateCreatePaymentSetupIntent(
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

    @POST("private/users/{userId}/payouts")
    suspend fun privateCreatePayoutMethod(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
        @Body body: CreatePayoutMethodRequest
    ): Response<CreatePayoutMethodResponse>

    @PATCH("private/users/{userId}/payments")
    suspend fun privateDeletePaymentMethod(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
        @Body body: DeletePaymentMethodRequest
    ): Response<DeletePaymentMethodResponse>

    @PATCH("private/users/{userId}/payouts")
    suspend fun privateDeletePayoutMethod(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
        @Body body: DeletePayoutMethodRequest
    ): Response<DeletePayoutMethodResponse>

    @POST("proxy-calls")
    suspend fun createProxyCall(
        @HeaderMap headers: Map<String, String>,
        @Body body: CreateProxyCallRequest
    ): Response<CreateProxyCallResponse>

    @GET("proxy-calls/{userId}")
    suspend fun readProxyCalls(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
    ): Response<ReadProxyCallsResponse>

    @GET("users")
    suspend fun readUsers(
        @HeaderMap headers: Map<String, String>
    ): Response<ReadUsersResponse>

    @POST("azcs/access-tokens")
    suspend fun azcsCreateAccessToken(
        @HeaderMap headers: Map<String, String>
    ): Response<CreateAZCSAccessTokenResponse>
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