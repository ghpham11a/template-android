package com.example.template.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toHourMinuteString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun LocalDateTime.toDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}

fun LocalDateTime.toDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return this.format(formatter)
}