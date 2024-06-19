package com.example.template

import android.app.Application
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        PaymentConfiguration.init(
            applicationContext,
            BuildConfig.STRIPE_API_KEY
        )
    }
}