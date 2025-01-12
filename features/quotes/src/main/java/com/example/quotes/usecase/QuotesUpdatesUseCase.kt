package com.example.quotes.usecase

import com.example.core.webscoket.BaseWebSocketEvent
import com.example.data.websocket.repository.QuotesUpdatesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuotesUpdatesUseCase @Inject constructor(
    private val repository: QuotesUpdatesRepository,
) {
    fun observeEvents(): Flow<BaseWebSocketEvent> = repository.observeEvents()
    fun sendMessage(message: String) = repository.sendMessage(message)
}