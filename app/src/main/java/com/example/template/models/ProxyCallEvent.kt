package com.example.template.models

data class ProxyCallEvent(
    var user: DynamoDBUser? = null,
    var proxyCall: ProxyCall? = null,
)