package com.example.template.repositories

import android.content.SharedPreferences
import com.amazonaws.mobile.client.results.Tokens
import com.example.template.models.CurrentUser
import com.example.template.models.ReadUserPrivateResponse
import com.example.template.models.ReadUserPublicResponse
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


class UserRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val TAG = "UserRepository"
    }

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: MutableStateFlow<Boolean> = _isAuthenticated

    private val _currentUser = MutableStateFlow(CurrentUser(isLoggedIn = false))
    val currentUser: MutableStateFlow<CurrentUser> = _currentUser

    val userSub: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_SUB, null)
    val username: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_USERNAME, null)
    val idToken: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_ID_TOKEN, null)


    private var userPrivate: ReadUserPrivateResponse? = null
    private var userPublic: ReadUserPublicResponse? = null

    suspend fun privateReadUser(refresh: Boolean = false): Response<ReadUserPrivateResponse>? {

        if (userPrivate != null && !refresh) {
            return Response.success(userPrivate)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.privateReadUser(
                    APIGateway.buildAuthorizedHeaders(idToken ?: ""),
                    userSub ?: ""
                )
                if (response.isSuccessful) {
                    userPrivate = response.body()
                }
                response
            } catch (e: Exception) {
                // Handle the exception
                null
            }
        }
    }

    suspend fun publicReadUser(userId: String, refresh: Boolean = false): Response<ReadUserPublicResponse>? {

        if (userPublic != null && !refresh) {
            return Response.success(userPublic)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.publicReadUser(
                    APIGateway.buildAuthorizedHeaders(idToken ?: ""),
                    userId
                )
                if (response.isSuccessful) {
                    userPublic = response.body()
                }
                response
            } catch (e: Exception) {
                // Handle the exception
                null
            }
        }
    }

//    fun checkIfTokenIsExpired(): Boolean {
//        val expirationDate = sharedPreferences.getString(SHARED_PREFERENCES_KEY_EXPIRATION_DATE, null)
//        val format = SimpleDateFormat(INTERNAL_DATE_PATTERN, Locale.getDefault())
//        val expiration = format.parse(expirationDate ?: "")
//        val now = System.currentTimeMillis()
//        return (expiration?.time ?: 0) < now
//    }

    fun isLoggedIn(): Boolean {
        _isAuthenticated.value = sharedPreferences.contains(SHARED_PREFERENCES_KEY_ID_TOKEN)
        return sharedPreferences.contains(SHARED_PREFERENCES_KEY_ID_TOKEN)
    }

    fun setLoggedIn(token: Tokens, username: String, userSub: String) {
        val format = SimpleDateFormat(INTERNAL_DATE_PATTERN, Locale.getDefault())
        val readableDate = format.format(token.idToken.expiration)

        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_ID_TOKEN, token.idToken.tokenString).apply()
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_ACCESS_TOKEN, token.accessToken.tokenString).apply()
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_USERNAME, username).apply()
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_SUB, userSub).apply()
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_EXPIRATION_DATE, readableDate).apply()

        _isAuthenticated.value = true
    }

    fun logOut() {
        _isAuthenticated.value = false
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_ID_TOKEN).apply()
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_ACCESS_TOKEN).apply()
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_USERNAME).apply()
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_EXPIRATION_DATE).apply()
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_SUB).apply()
    }
}