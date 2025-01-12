package com.example.core.webscoket

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WebSocketEventAdapter : JsonDeserializer<BaseWebSocketEvent> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): BaseWebSocketEvent {
        if (!json.isJsonArray) return BaseWebSocketEvent.Unknown

        val jsonArray = json.asJsonArray
        if (jsonArray.size() < 2) return BaseWebSocketEvent.Unknown

        val type = jsonArray[0].asString
        val data = jsonArray[1].asJsonObject

        return WebSocketEventFactory.createEvent(type, data, context)
    }
}