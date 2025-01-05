package com.example.core

import com.example.core.events.QuotesUpdatesData

sealed class WebSocketEvent: BaseWebSocketEvent {

    data object Unknown : WebSocketEvent()

    data class QuotesUpdateEvent(val data: QuotesUpdatesData) : WebSocketEvent() {
        companion object {
            const val TYPE = "q"
        }
    }

}