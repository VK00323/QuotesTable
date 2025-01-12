package com.example.core

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.core.webscoket.IWebSocketManager

class AppLifecycleObserver(
    private val webSocketManager: IWebSocketManager,
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        webSocketManager.connect(LifecycleStateEnum.ON_START)
    }

    override fun onStop(owner: LifecycleOwner) {
        webSocketManager.disconnect(LifecycleStateEnum.ON_STOP)
    }
}