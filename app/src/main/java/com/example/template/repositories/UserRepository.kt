package com.example.template.repositories

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_AUTH_TOKEN
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

    fun isLoggedIn(): Boolean {
        _isAuthenticated.value = sharedPreferences.contains(SHARED_PREFERENCES_KEY_AUTH_TOKEN)
        return sharedPreferences.contains(SHARED_PREFERENCES_KEY_AUTH_TOKEN)
    }

    fun setLoggedIn(token: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_AUTH_TOKEN, token).apply()
        _isAuthenticated.value = true
    }

    fun logOut() {
        _isAuthenticated.value = false
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_AUTH_TOKEN).apply()
    }
}