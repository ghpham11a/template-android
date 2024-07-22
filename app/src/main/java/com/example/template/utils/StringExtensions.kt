package com.example.template.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.extractNumbers(): String {
    val regex = Regex("\\d+")
    val matchResult = regex.find(this)
    return matchResult?.value ?: ""
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return LocalDateTime.parse(this, formatter)
}