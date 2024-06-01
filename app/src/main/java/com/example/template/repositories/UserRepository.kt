package com.example.template.repositories

import android.content.SharedPreferences
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_AUTH_TOKEN
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_USERNAME
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val TAG = "UserRepository"
    }

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: MutableStateFlow<Boolean> = _isAuthenticated

    val username: String?
        get() = sharedPreferences.getString(SHARED_PREFERENCES_KEY_USERNAME, null)

    fun isLoggedIn(): Boolean {
        _isAuthenticated.value = sharedPreferences.contains(SHARED_PREFERENCES_KEY_AUTH_TOKEN)
        return sharedPreferences.contains(SHARED_PREFERENCES_KEY_AUTH_TOKEN)
    }

    fun setLoggedIn(token: String, username: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_AUTH_TOKEN, token).apply()
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_USERNAME, username).apply()
        _isAuthenticated.value = true
    }

    fun logOut() {
        _isAuthenticated.value = false
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_AUTH_TOKEN).apply()
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_USERNAME).apply()
    }
}