package com.example.template.utils

object Constants {

    const val SHARED_PREFERENCES_KEY_AUTH_TOKEN = "auth_token"
    const val SHARED_PREFERENCES_NAME = "template_shared_preferences"
    const val USER_NOT_CONFIRMED_INDICATOR = "not confirmed"
    const val AWS_COGNITO_USER_DOES_EXIST_MESSAGE = "User exists"
    const val AWS_COGNITO_USER_DOES_NOT_EXIST_MESSAGE = "User does not exist"

    val BOTTOM_NAVIGATION_ROUTES = listOf<String>(
        Route.ALPHA_TAB,
        Route.BRAVO_TAB,
        Route.CHARLIE_TAB,
        Route.DELTA_TAB
    )
    object Route {
        const val ALPHA_TAB = "alpha_tab"
        const val BRAVO_TAB = "bravo_tab"
        const val BRAVO_LIST = "bravo_list"
        const val BRAVO_DETAILS = "bravo_details"
        const val CHARLIE_TAB = "charlie_tab"
        const val DELTA_TAB = "delta_tab"

        const val AUTH = "auth"
        const val AUTH_HUB = "auth/hub"
        const val AUTH_CODE_VERIFICATION = "auth/code_verification/%s/%s"
        const val AUTH_ENTER_PASSWORD = "auth/enter_password/%s"
        const val AUTH_ADD_INFO = "auth/add_info/%s"

        const val SNAG = "snag?message={message}"
        const val SNAG_FORMAT = "snag?message=%s"
    }
}