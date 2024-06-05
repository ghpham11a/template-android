package com.example.template.utils

object Constants {

    const val SHARED_PREFERENCES_NAME = "template_shared_preferences"
    const val SHARED_PREFERENCES_KEY_USERNAME = "username"
    const val SHARED_PREFERENCES_KEY_ACCESS_TOKEN = "access_token"
    const val SHARED_PREFERENCES_KEY_ID_TOKEN = "id_token"
    const val SHARED_PREFERENCES_KEY_EXPIRATION_DATE = "expiration_date"
    const val SHARED_PREFERENCES_KEY_SUB = "sub"
    const val INTERNAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val USER_NOT_CONFIRMED_INDICATOR = "not confirmed"
    const val AWS_COGNITO_USER_DOES_EXIST_MESSAGE = "User exists"
    const val AWS_COGNITO_USER_DOES_NOT_EXIST_MESSAGE = "User does not exist"
    const val USER_IMAGE_URL = "https://template-public-resources.s3.amazonaws.com/%s.jpg"

    val BOTTOM_NAVIGATION_ROUTES = listOf<String>(
        Route.HOME_TAB,
        Route.HOME_HUB,
        Route.FEATURES_TAB,
        Route.FEATURES_LIST,
        Route.PROFILE_TAB,
        Route.PROFILE_HUB
    )
    object Route {
        const val HOME_TAB = "home_tab"
        const val HOME_HUB = "home_tab/hub"
        const val FEATURES_TAB = "features_tab"
        const val FEATURES_LIST = "features_list"
        const val BRAVO_DETAILS = "bravo_details"
        const val PROFILE_TAB = "profile_tab"

        const val PROFILE_HUB = "profile_tab/hub"
        const val PUBLIC_PROFILE = "profile_tab/public_profile/%s"
        const val EDIT_PROFILE = "profile_tab/edit_profile"
        const val LOGIN_AND_SECURITY = "profile_tab/login_and_security"
        const val PAYMENTS_AND_PAYOUTS = "profile_tab/payments_and_payouts"

        const val RESET_PASSWORD = "auth/hub/reset_password"
        const val RESET_PASSWORD_SUCCESS = "reset_password_success"

        const val AUTH = "auth"
        const val AUTH_HUB = "auth/hub"
        const val NEW_PASSWORD = "auth/new_password/%s/%s"
        const val CODE_VERIFICATION = "auth/code_verification/%s/%s/%s"
        const val AUTH_ENTER_PASSWORD = "auth/enter_password/%s"
        const val AUTH_ADD_INFO = "auth/add_info/%s"

        const val SNAG = "snag?message={message}"
        const val SNAG_FORMAT = "snag?message=%s"
    }
}