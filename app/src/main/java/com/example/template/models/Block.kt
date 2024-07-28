package com.example.template.models

import java.time.LocalDateTime

data class Block(
    var key: String,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
)
