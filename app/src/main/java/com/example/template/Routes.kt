package com.example.template

import com.example.template.utils.Constants

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Features : Screen("features")
    object Profile : Screen("profile")
    object PublicProfile : Screen("public-profile/{userId}") {
        fun build(userId: String) = "public-profile/$userId"
    }
    object EditProfile : Screen("edit-profile")
    object FilterList : Screen("filter-list")
    object LoginAndSecurity : Screen("login_and_security")
    object PaymentsAndPayouts : Screen("payments-and-payouts")
    object ResetPassword : Screen("reset-password")
    object ResetPasswordSuccess : Screen("reset-password-success")
    object Thing : Screen("thing")
    object ThingBuilder: Screen("thing-builder/{mode}?steps={steps}") {
        fun build(mode: String, steps: String) = "thing-builder/$mode?steps=$steps"
    }
    object AuthHub : Screen("auth-hub")
    object NewPassword : Screen("auth-hub/{username}/{code}") {
        fun build(username: String, code: String) = "auth-hub/$username/$code"
    }
    object CodeVerification : Screen("code-verification/{verificationType}/{username}/{password}") {
        fun build(verificationType: String, username: String, password: String) =
            "code-verification/$verificationType/$username/$password"
    }
    object EnterPassword : Screen("enter-password/{username}") {
        fun build(username: String) = "enter-password/$username"
    }
    object AddNewUserInfo : Screen("add-new-user-info/{username}/{phoneNumber}") {
        fun build(username: String = Constants.PATH_PLACEHOLDER, phoneNumber: String = Constants.PATH_PLACEHOLDER) = "add-new-user-info/$username/${phoneNumber}"
    }
    object Snag : Screen("snag")
    object PersonalInfo : Screen("personal-info")
}