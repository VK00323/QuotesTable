package com.example.data.websocket.repository

import com.example.core.webscoket.BaseWebSocketEvent
import com.example.core.webscoket.IWebSocketManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuotesUpdatesRepository @Inject constructor(
    private val webSocketManager: IWebSocketManager,
) {
    fun observeEvents(): Flow<BaseWebSocketEvent> = webSocketManager.observeEvents()
    fun sendMessage(message: String) = webSocketManager.sendMessage(message)
}