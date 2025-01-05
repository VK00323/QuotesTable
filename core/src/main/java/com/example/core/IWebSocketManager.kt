package com.example.core

import kotlinx.coroutines.flow.Flow

interface IWebSocketManager {
    fun connect()
    fun disconnect()
    fun sendMessage(message: String)
    fun observeEvents(): Flow<WebSocketEvent>
}