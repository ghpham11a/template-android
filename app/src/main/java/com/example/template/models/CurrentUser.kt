package com.example.template.models

data class CurrentUser(
    var sub: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var idToken: String? = null,
    var accessToken: String? = null,
    var isLoggedIn: Boolean = false
)
