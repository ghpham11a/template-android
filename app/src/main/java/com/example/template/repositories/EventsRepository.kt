package com.example.template.repositories

import android.content.SharedPreferences
import com.example.template.models.CreateThingResponse
import com.example.template.models.ProxyCall
import com.example.template.models.ProxyCallEvent
import com.example.template.models.Thing
import com.example.template.models.VideoCall
import com.example.template.networking.APIGateway
import com.example.template.networking.APIGatewayEvents
import com.example.template.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


class EventsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    val userId: String?
        get() = sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_SUB, null)
    val username: String?
        get() = sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_USERNAME, null)
    val idToken: String?
        get() = sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_ID_TOKEN, null)

    var proxyCalls: List<ProxyCall>? = null
    var videoCalls: List<VideoCall>? = null

    suspend fun fetchProxyCalls(refresh: Boolean = false): List<ProxyCall>? {

        if (proxyCalls != null && !refresh) {
            return proxyCalls ?: emptyList()
        }

        val response = APIGatewayEvents.api.readProxyCalls(
            APIGateway.buildAuthorizedHeaders(idToken ?: ""),
            userId ?: ""
        )
        if (response.isSuccessful) {
            proxyCalls = response.body()?.calls
            return proxyCalls
        } else {
            return null
        }
    }

    suspend fun fetchVideoCalls(refresh: Boolean = false): List<VideoCall>? {

        if (videoCalls != null && !refresh) {
            return videoCalls ?: emptyList()
        }

        val response = APIGatewayEvents.api.readVideoCalls(
            APIGateway.buildAuthorizedHeaders(idToken ?: ""),
            userId ?: ""
        )
        if (response.isSuccessful) {
            videoCalls = response.body()?.calls
            return videoCalls
        } else {
            return null
        }
    }
}