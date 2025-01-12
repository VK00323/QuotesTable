package com.example.core.webscoket

import com.example.core.LifecycleStateEnum
import kotlinx.coroutines.flow.Flow

interface IWebSocketManager {
    fun connect(lifecycleState: LifecycleStateEnum? = null)
    fun disconnect(lifecycleState: LifecycleStateEnum? = null)
    fun sendMessage(message: String)
    fun observeEvents(): Flow<BaseWebSocketEvent>
}