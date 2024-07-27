package com.example.template.models

data class ChatEvent(
    var user: DynamoDBUser? = null,
    var chat: Chat? = null
)
