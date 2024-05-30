package com.example.template.networking

import com.example.template.models.CheckIfUserExistsResponse
import com.example.template.models.Todo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LambdaService {
    @GET("test")
    suspend fun checkIfUserExists(
        @Query("username") username: String
    ): Response<CheckIfUserExistsResponse>
}

// Create Retrofit instance
object Lambdas {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://o7hniu19pc.execute-api.us-east-1.amazonaws.com/QA/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LambdaService by lazy {
        retrofit.create(LambdaService::class.java)
    }
}