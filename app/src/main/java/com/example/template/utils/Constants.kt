package com.example.template.utils

import com.example.template.R
import com.example.template.Screen
import com.google.gson.annotations.SerializedName

object Constants {

    object UserAttributes {
        const val UserId = "userId"
        const val Email = "email"
        const val FirstName = "firstName"
        const val LastName = "lastName"
        const val PreferredName = "preferredName"
    }


    const val SHARED_PREFERENCES_NAME = "template_shared_preferences"
    const val SHARED_PREFERENCES_KEY_USERNAME = "username"
    const val SHARED_PREFERENCES_KEY_ACCESS_TOKEN = "access_token"
    const val SHARED_PREFERENCES_KEY_ID_TOKEN = "id_token"
    const val SHARED_PREFERENCES_KEY_EXPIRATION_DATE = "expiration_date"
    const val SHARED_PREFERENCES_KEY_SUB = "sub"
    const val SHARED_PREFERENCES_KEY_BIRTHDATE = "birthdate"
    const val SHARED_PREFERENCES_KEY_FIRSTNAME = "given_name"
    const val SHARED_PREFERENCES_KEY_LASTNAME = "family_name"
    const val SHARED_PREFERENCES_KEY_AZCS_ACCESSTOKEN = "SHARED_PREFERENCES_KEY_AZCS_ACCESSTOKEN"
    const val INTERNAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val USER_NOT_CONFIRMED_INDICATOR = "not confirmed"
    const val AWS_COGNITO_USER_DOES_EXIST_MESSAGE = "User exists"
    const val AWS_COGNITO_USER_DOES_NOT_EXIST_MESSAGE = "User does not exist"
    const val USER_IMAGE_URL = "https://template-public-resources.s3.amazonaws.com/%s.jpg"
    const val PATH_PLACEHOLDER = "NONE"

    val BOTTOM_NAVIGATION_ROUTES = listOf<String>(
        Screen.Features.route,
        Screen.Profile.route,
    )

    object ThingScreen {
        const val THING_TYPE = "THING_TYPE"
        const val THING_DESCRIPTION = "THING_DESCRIPTION"
        const val THING_METHODS = "THING_METHODS"
        const val THING_LOCATION = "THING_LOCATION"
        const val THING_TIME = "THING_TIME"
    }

    val COUNTRY_CODES = listOf(
        "United States ( +1 )",
        "India ( +91 )",
        "Isle of Man ( +44 )",
        "Austalia ( +61 )",
        "Japan ( +81 )"
    )

    val COUNTRIES = listOf(
        "United States",
        "United Kingdom"
    )

    val TAGS = listOf<Pair<Int, Int>>(
        Pair(1, R.string.tag_alpha),
        Pair(2, R.string.tag_bravo),
        Pair(3, R.string.tag_charlie),
        Pair(4, R.string.tag_delta),
        Pair(5, R.string.tag_echo),
    )

}