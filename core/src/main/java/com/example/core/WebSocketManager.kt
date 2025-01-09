package com.example.core

import android.util.Log
import com.example.core.di.WebSocketUrl
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    @WebSocketUrl private val url: String,
    private val client: OkHttpClient,
    private val gson: Gson,
) : IWebSocketManager {

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var messageQueue = mutableListOf<String>()
    private val messages = MutableSharedFlow<String>()

    @Volatile
    private var currentLifecycleState: LifecycleStateEnum? = null

    @Synchronized
    private fun sendAllSavedMessage() {
        messageQueue.forEach { message ->
            webSocket?.send(message)
        }
    }

    override fun connect(lifecycleState: LifecycleStateEnum?) {
        disconnect(lifecycleState)
        if (lifecycleState == LifecycleStateEnum.ON_START) {
            val request = Request.Builder().url(url).build()
            webSocket = client.newWebSocket(request, WebSocketListenerImpl())
        }
    }

    override fun observeEvents(): Flow<WebSocketEvent> = messages.map { parseMessage(it) }

    override fun disconnect(lifecycleState: LifecycleStateEnum?) {
        lifecycleState?.let { currentLifecycleState = it }
        webSocket?.close(1000, "Normal Closure")
        webSocket = null
    }

    @Synchronized
    override fun sendMessage(message: String) {
        messageQueue.add(message)
        webSocket?.send(message)
    }

    private inner class WebSocketListenerImpl : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            sendAllSavedMessage()
            Log.d("WebSocketManager", "WebSocketManager status: onOpen")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            scope.launch {
                messages.emit(text)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            reconnectWithDelay()
            Log.d("WebSocketManager", "WebSocketManager status: onFailure ${t.message}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            Log.d("WebSocketManager", "WebSocketManager status: onClosing $reason")
        }
    }

    private fun reconnectWithDelay() {
        scope.launch {
            delay(2000)
            if (currentLifecycleState == LifecycleStateEnum.ON_START) {
                connect(currentLifecycleState)
            }
        }
    }

    //TODO Event по идее должны быть в data слое
    private fun parseMessage(message: String): WebSocketEvent {
        return try {
            gson.fromJson(message, WebSocketEvent::class.java)
        } catch (e: Exception) {
            WebSocketEvent.Unknown
        }
    }
}