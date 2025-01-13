package com.example.core.webscoket

import com.example.core.LifecycleStateEnum
import kotlinx.coroutines.flow.Flow

interface IWebSocketManager {
    fun connect(lifecycleState: LifecycleStateEnum)
    fun disconnect(lifecycleState: LifecycleStateEnum)
    fun sendMessage(message: String)
    fun observeEvents(): Flow<BaseWebSocketEvent>
}