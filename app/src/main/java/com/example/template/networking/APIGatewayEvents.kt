package com.example.template.networking

import com.example.template.models.CreateProxyCallRequest
import com.example.template.models.CreateProxyCallResponse
import com.example.template.models.CreateVideoCallRequest
import com.example.template.models.CreateVideoCallResponse
import com.example.template.models.ReadProxyCallsResponse
import com.example.template.models.ReadVideoCallsResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path

interface APIGatewayEventsService {
    @POST("events/proxy-calls")
    suspend fun createProxyCall(
        @HeaderMap headers: Map<String, String>,
        @Body body: CreateProxyCallRequest
    ): Response<CreateProxyCallResponse>

    @GET("events/proxy-calls/{userId}")
    suspend fun readProxyCalls(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
    ): Response<ReadProxyCallsResponse>

    @POST("events/video-calls")
    suspend fun createVideoCall(
        @HeaderMap headers: Map<String, String>,
        @Body body: CreateVideoCallRequest
    ): Response<CreateVideoCallResponse>

    @GET("events/video-calls/{userId}")
    suspend fun readVideoCalls(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String,
    ): Response<ReadVideoCallsResponse>
}

// Create Retrofit instance
object APIGatewayEvents {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://gviivl9o82.execute-api.us-east-1.amazonaws.com/qa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: APIGatewayEventsService by lazy {
        retrofit.create(APIGatewayEventsService::class.java)
    }
}