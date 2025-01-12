package com.example.core.webscoket

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject

interface WebSocketEventHandler<T : BaseWebSocketEvent> {
    val type: String
    fun handle(data: JsonObject, context: JsonDeserializationContext): T
}