package com.example.template.ui.screens.schedulerhub.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val DATE_FORMAT = "yyyy-MM-dd"
val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"
val TIME_FORMAT = "HH:mm"

class StartTimeBlock(
    var id: String? = null,
    var start: LocalDateTime = LocalDateTime.now(),
    var end: LocalDateTime = LocalDateTime.now()
) {
    var isSelectable: Boolean = true

    override fun toString(): String {
        val timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT)
        return this.start.format(timeFormatter)
    }
}