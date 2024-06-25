package com.example.template.networking

import com.example.template.models.GoogleGeocodeResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleMapsService {

    @GET("api/geocode/json")
    suspend fun readPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") key: String,
    ): GoogleGeocodeResponse
}

// Create Retrofit instance
object GoogleMaps {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GoogleMapsService by lazy {
        retrofit.create(GoogleMapsService::class.java)
    }
}