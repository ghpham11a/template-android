package com.example.template.utils

fun String.extractNumbers(): String {
    val regex = Regex("\\d+")
    val matchResult = regex.find(this)
    return matchResult?.value ?: ""
}