package com.example.template.repositories

import android.content.SharedPreferences
import com.amazonaws.mobile.client.results.Tokens
import com.example.template.models.CurrentUser
import com.example.template.models.ReadUserPrivateResponse
import com.example.template.models.ReadUserPublicResponse
import com.example.template.networking.APIGateway
import com.example.template.utils.Constants
import com.example.template.utils.Constants.INTERNAL_DATE_PATTERN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_ACCESS_TOKEN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_BIRTHDATE
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_ID_TOKEN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_EXPIRATION_DATE
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_FIRSTNAME
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_LASTNAME
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_SUB
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_USERNAME
import com.example.template.utils.removeValues
import com.example.template.utils.updateAccessToken
import com.example.template.utils.updateBirthdate
import com.example.template.utils.updateEmail
import com.example.template.utils.updateExpirationDate
import com.example.template.utils.updateFirstName
import com.example.template.utils.updateIdToken
import com.example.template.utils.updateLastName
import com.example.template.utils.updateUserId
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

    val userId: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_SUB, null)
    val username: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_USERNAME, null)
    val idToken: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_ID_TOKEN, null)
    val firstName: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_FIRSTNAME, null)
    val lastName: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_LASTNAME, null)
    val birthDate: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_BIRTHDATE, null)


    var userPrivate: ReadUserPrivateResponse? = null
    var userPublic: ReadUserPublicResponse? = null

    suspend fun privateReadUser(refresh: Boolean = false): Response<ReadUserPrivateResponse>? {

        if (userPrivate != null && !refresh) {
            return Response.success(userPrivate)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = APIGateway.api.privateReadUser(
                    APIGateway.buildAuthorizedHeaders(idToken ?: ""),
                    userId ?: ""
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

    fun setLoggedIn(token: Tokens, username: String, userAttributes: Map<String, String>) {
        val format = SimpleDateFormat(INTERNAL_DATE_PATTERN, Locale.getDefault())
        val readableDate = format.format(token.idToken.expiration)

        sharedPreferences.updateUserId(userAttributes["sub"] ?: "")
        sharedPreferences.updateIdToken(token.idToken.tokenString)
        sharedPreferences.updateAccessToken(token.accessToken.tokenString)
        sharedPreferences.updateEmail(userAttributes["email"] ?: "")
        sharedPreferences.updateFirstName(userAttributes["given_name"] ?: "")
        sharedPreferences.updateLastName(userAttributes["family_name"] ?: "")
        sharedPreferences.updateBirthdate(userAttributes["birthdate"] ?: "")
        sharedPreferences.updateExpirationDate(readableDate)

        _isAuthenticated.value = true
    }

    fun updateUser(key: String, value: String) {
        when (key) {
            SHARED_PREFERENCES_KEY_SUB -> sharedPreferences.updateUserId(value)
            SHARED_PREFERENCES_KEY_ID_TOKEN -> sharedPreferences.updateIdToken(value)
            SHARED_PREFERENCES_KEY_ACCESS_TOKEN -> sharedPreferences.updateAccessToken(value)
            SHARED_PREFERENCES_KEY_USERNAME -> {
                userPrivate?.user?.email = value
                sharedPreferences.updateEmail(value)
            }
            Constants.UserAttributes.FirstName -> {
                userPrivate?.user?.lastName = value
            }
            Constants.UserAttributes.LastName -> {
                userPrivate?.user?.lastName = value
            }
            Constants.UserAttributes.PreferredName -> {
                userPrivate?.user?.preferredName = value
            }
            SHARED_PREFERENCES_KEY_BIRTHDATE -> sharedPreferences.updateBirthdate(value)
            SHARED_PREFERENCES_KEY_EXPIRATION_DATE -> sharedPreferences.updateExpirationDate(value)
        }
    }

    fun logOut() {
        _isAuthenticated.value = false
        sharedPreferences.removeValues()
    }
}