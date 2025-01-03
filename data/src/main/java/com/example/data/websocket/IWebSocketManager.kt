package com.example.data.websocket

import kotlinx.coroutines.flow.Flow

interface IWebSocketManager {
    fun connect()
    fun disconnect()
    fun sendMessage(message: String)
    fun observeEvents(): Flow<WebSocketEvent>
}