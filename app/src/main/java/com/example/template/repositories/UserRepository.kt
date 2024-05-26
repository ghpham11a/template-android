package com.example.template.repositories

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.template.utils.Constants.SHARED_PREFERENCES_KEY_AUTH_TOKEN
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val TAG = "UserRepository"
    }

    private val _isAuthenticated = MutableLiveData(false)
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains(SHARED_PREFERENCES_KEY_AUTH_TOKEN)
    }

    fun setLoggedIn(token: String) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_AUTH_TOKEN, token).apply()
    }

    fun logOut() {
        sharedPreferences.edit().remove(SHARED_PREFERENCES_KEY_AUTH_TOKEN).apply()
    }
}