package com.example.template.repositories

import android.content.SharedPreferences
import com.amazonaws.mobile.client.results.Tokens
import com.example.template.models.CreateThingResponse
import com.example.template.models.CurrentUser
import com.example.template.models.ReadUserPrivateResponse
import com.example.template.models.ReadUserPublicResponse
import com.example.template.models.Thing
import com.example.template.networking.APIGateway
import com.example.template.utils.Constants.INTERNAL_DATE_PATTERN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_ACCESS_TOKEN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_ID_TOKEN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_EXPIRATION_DATE
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_SUB
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_USERNAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


class ThingRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    suspend fun createThing(thing: Thing): Response<CreateThingResponse>? {

        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.privateCreateThing(
                    APIGateway.buildAuthorizedHeaders(sharedPreferences.getString(SHARED_PREFERENCES_KEY_ID_TOKEN, null) ?: ""),
                    thing
                )
                response
            } catch (e: Exception) {
                // Handle the exception
                null
            }
        }
    }
}