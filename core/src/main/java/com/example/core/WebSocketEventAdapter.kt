package com.example.core

import com.example.core.events.QuotesUpdatesData
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WebSocketEventAdapter : JsonDeserializer<WebSocketEvent> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): WebSocketEvent {
        if (!json.isJsonArray) return WebSocketEvent.Unknown

        val jsonArray = json.asJsonArray
        if (jsonArray.size() < 2) return WebSocketEvent.Unknown

        val type = jsonArray[0].asString
        val data = jsonArray[1].asJsonObject

        return when (type) {
            // Todo можно ли сделать более расширяемым
            WebSocketEvent.QuotesUpdateEvent.TYPE -> {
                val eventData = context.deserialize<QuotesUpdatesData>(data, QuotesUpdatesData::class.java)
                WebSocketEvent.QuotesUpdateEvent(eventData)
            }

            else -> WebSocketEvent.Unknown
        }
    }
}