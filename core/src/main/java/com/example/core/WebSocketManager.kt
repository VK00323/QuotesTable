package com.example.core

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

class WebSocketManager @Inject constructor (
    @WebSocketUrl private val url: String,
    private val client: OkHttpClient,
    private val gson: Gson,
): IWebSocketManager {

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val messages = MutableSharedFlow<String>()

    override fun observeEvents(): Flow<WebSocketEvent> = messages.map { parseMessage(it) }

    override fun connect() {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, WebSocketListenerImpl())
    }

    override fun disconnect() {
        webSocket?.close(1000, "Normal Closure")
        webSocket = null
    }

    override fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    private inner class WebSocketListenerImpl : WebSocketListener() {

        override fun onMessage(webSocket: WebSocket, text: String) {
            scope.launch {
                messages.emit(text)
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            reconnectWithDelay()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
        }
    }

    private fun reconnectWithDelay() {
        scope.launch {
            delay(2000)
            connect()
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