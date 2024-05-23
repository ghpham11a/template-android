package com.example.template.networking

import com.example.template.models.Todo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface JSONPlaceholderService {
    @GET("todos")
    suspend fun getTodo(): Response<List<Todo>>
}

// Create Retrofit instance
object JSONPlaceholder {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: JSONPlaceholderService by lazy {
        retrofit.create(JSONPlaceholderService::class.java)
    }
}