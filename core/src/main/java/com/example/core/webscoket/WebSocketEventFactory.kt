package com.example.core.webscoket

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject

object WebSocketEventFactory {
    private val handlers = mutableListOf<WebSocketEventHandler<out BaseWebSocketEvent>>()

    fun register(handler: WebSocketEventHandler<out BaseWebSocketEvent>) {
        handlers.add(handler)
    }

    fun createEvent(
        type: String,
        data: JsonObject,
        context: JsonDeserializationContext,
    ): BaseWebSocketEvent {
        val handler = handlers.find { it.type == type }
        return handler?.handle(data, context) ?: BaseWebSocketEvent.Unknown
    }
}
