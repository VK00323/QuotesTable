package com.example.data.websocket.repository

import com.example.core.IWebSocketManager
import com.example.core.WebSocketEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuotesUpdatesRepository @Inject constructor(
    private val webSocketManager: IWebSocketManager,
) {
    fun observeEvents(): Flow<WebSocketEvent> = webSocketManager.observeEvents()
    fun sendMessage(message: String) = webSocketManager.sendMessage(message)
}