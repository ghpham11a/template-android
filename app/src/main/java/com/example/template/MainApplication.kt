package com.example.template

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // To use the Stripe SDK / APIs
        PaymentConfiguration.init(
            applicationContext,
            BuildConfig.STRIPE_API_KEY
        )

        // To use the Google Places SDK / APIs
        Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.MAPS_API_KEY);



    }
}