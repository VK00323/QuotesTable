package com.example.data.websocket

import com.example.data.websocket.events.QuotesUpdatesData

sealed class WebSocketEvent {

    data object Unknown : WebSocketEvent()

    data class QuotesUpdateEvent(val data: QuotesUpdatesData) : WebSocketEvent() {
        companion object {
            const val TYPE = "q"
        }
    }

}