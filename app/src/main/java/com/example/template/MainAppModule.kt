package com.example.template

import android.content.Context
import android.content.SharedPreferences
import com.example.template.repositories.EventsRepository
import com.example.template.repositories.SchedulesRepository
import com.example.template.repositories.UserRepository
import com.example.template.ui.screens.schedulerhub.SchedulerHubScreen
import com.example.template.utils.Constants.SHARED_PREFERENCES_NAME
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainAppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideEventsRepository(sharedPreferences: SharedPreferences): EventsRepository {
        return EventsRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSchedulerRepository(sharedPreferences: SharedPreferences): SchedulesRepository {
        return SchedulesRepository(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        return Places.createClient(context)
    }
}