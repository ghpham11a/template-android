package com.example.template.utils

import android.content.SharedPreferences

fun SharedPreferences.updateUserId(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_SUB, value).apply()
}
fun SharedPreferences.updateIdToken(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_ID_TOKEN, value).apply()
}
fun SharedPreferences.updateAccessToken(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_ACCESS_TOKEN, value).apply()
}
fun SharedPreferences.updateEmail(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_USERNAME, value).apply()
}
fun SharedPreferences.updateLastName(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_LASTNAME, value).apply()
}
fun SharedPreferences.updateFirstName(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_FIRSTNAME, value).apply()
}
fun SharedPreferences.updateBirthdate(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_BIRTHDATE, value).apply()
}
fun SharedPreferences.updateExpirationDate(value: String) {
    this.edit().putString(Constants.SHARED_PREFERENCES_KEY_EXPIRATION_DATE, value).apply()
}

fun SharedPreferences.removeValues() {
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_ID_TOKEN).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_ACCESS_TOKEN).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_USERNAME).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_EXPIRATION_DATE).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_SUB).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_BIRTHDATE).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_FIRSTNAME).apply()
    this.edit().remove(Constants.SHARED_PREFERENCES_KEY_LASTNAME).apply()
}


