package com.example.data.websocket.events

import com.example.core.webscoket.BaseWebSocketEvent
import com.example.data.websocket.dto.events.QuotesUpdatesDataDto
import com.example.data.websocket.entities.QuotesUpdatesData
import com.example.data.websocket.mappers.mapToQuotesUpdatesData

sealed class WebSocketEvent : BaseWebSocketEvent {

    data class QuotesUpdateEvent(val data: QuotesUpdatesData) : WebSocketEvent() {
        companion object {
            const val TYPE = "q"

            fun fromDto(dto: QuotesUpdatesDataDto): QuotesUpdateEvent {
                return QuotesUpdateEvent(data = dto.mapToQuotesUpdatesData())
            }
        }
    }
}