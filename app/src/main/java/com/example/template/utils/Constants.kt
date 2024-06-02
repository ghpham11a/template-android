package com.example.template.utils

object Constants {

    const val SHARED_PREFERENCES_NAME = "template_shared_preferences"
    const val SHARED_PREFERENCES_KEY_USERNAME = "username"
    const val SHARED_PREFERENCES_KEY_ACCESS_TOKEN = "access_token"
    const val SHARED_PREFERENCES_KEY_ID_TOKEN = "id_token"
    const val SHARED_PREFERENCES_KEY_EXPIRATION_DATE = "expiration_date"
    const val INTERNAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val USER_NOT_CONFIRMED_INDICATOR = "not confirmed"
    const val AWS_COGNITO_USER_DOES_EXIST_MESSAGE = "User exists"
    const val AWS_COGNITO_USER_DOES_NOT_EXIST_MESSAGE = "User does not exist"

    val BOTTOM_NAVIGATION_ROUTES = listOf<String>(
        Route.HOME_TAB,
        Route.FEATURES_TAB,
        Route.PROFILE_TAB
    )
    object Route {
        const val HOME_TAB = "home_tab"
        const val FEATURES_TAB = "features_tab"
        const val FEATURES_LIST = "features_list"
        const val BRAVO_DETAILS = "bravo_details"
        const val PROFILE_TAB = "profile_tab"

        const val AUTH = "auth"
        const val AUTH_HUB = "auth/hub"
        const val AUTH_CODE_VERIFICATION = "auth/code_verification/%s/%s"
        const val AUTH_ENTER_PASSWORD = "auth/enter_password/%s"
        const val AUTH_ADD_INFO = "auth/add_info/%s"

        const val SNAG = "snag?message={message}"
        const val SNAG_FORMAT = "snag?message=%s"
    }
}