package com.example.quotes.usecase

import com.example.data.websocket.repository.QuotesUpdatesRepository
import javax.inject.Inject

class SendWebSocketMessageUseCase @Inject constructor(
    private val repository: QuotesUpdatesRepository
) {
    operator fun invoke(message: String) = repository.sendMessage(message)
}