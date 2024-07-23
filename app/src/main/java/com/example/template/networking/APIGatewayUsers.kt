package com.example.template.networking

import com.example.template.models.CreateProxyCallRequest
import com.example.template.models.CreateProxyCallResponse
import com.example.template.models.ReadProxyCallsResponse
import com.example.template.models.ReadUsersResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path

interface APIGatewayUsersService {
    @GET("users")
    suspend fun readUsers(
        @HeaderMap headers: Map<String, String>
    ): Response<ReadUsersResponse>
}

// Create Retrofit instance
object APIGatewayUsers {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://gviivl9o82.execute-api.us-east-1.amazonaws.com/qa/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: APIGatewayUsersService by lazy {
        retrofit.create(APIGatewayUsersService::class.java)
    }
}