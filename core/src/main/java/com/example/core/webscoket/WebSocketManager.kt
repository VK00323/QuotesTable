package com.example.core.webscoket

import com.example.core.LifecycleStateEnum
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

    private companion object{
        private const val STATUS_CODE = 1000
    }

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var messageQueue = mutableListOf<String>()
    private val messages = MutableSharedFlow<String>(replay = 1)

    @Volatile
    private var currentLifecycleState: LifecycleStateEnum? = null

    @Synchronized
    private fun sendAllSavedMessage() {
        messageQueue.forEach { message ->
            webSocket?.send(message)
        }
    }

    override fun connect(lifecycleState: LifecycleStateEnum) {
        disconnect(lifecycleState)
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, WebSocketListenerImpl())
    }

    override fun observeEvents(): Flow<BaseWebSocketEvent> = messages.map { parseMessage(it) }

    override fun disconnect(lifecycleState: LifecycleStateEnum) {
            currentLifecycleState = lifecycleState
            webSocket?.close(STATUS_CODE, "Normal Closure")
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
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            scope.launch {
                messages.emit(text)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            reconnectWithDelay()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(STATUS_CODE, null)
        }
    }

    private fun reconnectWithDelay() {
        scope.launch {
            delay(3000)
            if (currentLifecycleState == LifecycleStateEnum.ON_START) {
                connect(LifecycleStateEnum.ON_START)
            }
        }
    }

    private fun parseMessage(message: String): BaseWebSocketEvent {
        return try {
            gson.fromJson(message, BaseWebSocketEvent::class.java)
        } catch (e: Exception) {
            BaseWebSocketEvent.Unknown
        }
    }
}