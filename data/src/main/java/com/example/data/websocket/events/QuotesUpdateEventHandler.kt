package com.example.data.websocket.events

import com.example.core.webscoket.WebSocketEventHandler
import com.example.data.websocket.dto.events.QuotesUpdatesDataDto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject

class QuotesUpdateEventHandler : WebSocketEventHandler<WebSocketEvent.QuotesUpdateEvent> {
    override val type: String = WebSocketEvent.QuotesUpdateEvent.TYPE

    override fun handle(
        data: JsonObject,
        context: JsonDeserializationContext,
    ): WebSocketEvent.QuotesUpdateEvent {
        val eventDataDto =
            context.deserialize<QuotesUpdatesDataDto>(data, QuotesUpdatesDataDto::class.java)
        return WebSocketEvent.QuotesUpdateEvent.fromDto(eventDataDto)
    }
}