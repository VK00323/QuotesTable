package com.example.quotes.usecase

import com.example.data.websocket.entities.QuotesUpdatesData
import com.example.data.websocket.events.WebSocketEvent
import com.example.data.websocket.repository.QuotesUpdatesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuotesUpdatesUseCase @Inject constructor(
    private val repository: QuotesUpdatesRepository,
) {
    private companion object {
        private const val QUOTES_UPDATES_ACCUMULATION_DELAY = 1000L
    }

    fun observeQuotesUpdateEvents(): Flow<List<QuotesUpdatesData>> = flow {
        val buffer = mutableMapOf<String, QuotesUpdatesData>()

        CoroutineScope(Dispatchers.IO).launch {
            repository.observeEvents()
                .filterIsInstance<WebSocketEvent.QuotesUpdateEvent>()
                .collect { push ->
                    synchronized(buffer) {
                        val eventData = push.data
                        val existingPush = buffer[eventData.ticker]
                        buffer[eventData.ticker] = if (existingPush != null) {
                            mergePushes(existingPush, eventData)
                        } else {
                            eventData
                        }
                    }
                }
        }

        while (true) {
            delay(QUOTES_UPDATES_ACCUMULATION_DELAY)
            val pack = synchronized(buffer) {
                val collectedData = buffer.values.toList()
                buffer.clear()
                collectedData
            }
            emit(pack)
        }
    }.flowOn(Dispatchers.IO)

    private fun mergePushes(
        oldPush: QuotesUpdatesData,
        newPush: QuotesUpdatesData,
    ): QuotesUpdatesData {
        return oldPush.copy(
            exchangeLatestTrade = newPush.exchangeLatestTrade ?: oldPush.exchangeLatestTrade,
            nameLatin = newPush.nameLatin ?: oldPush.nameLatin,
            minStep = newPush.minStep ?: oldPush.minStep,
            lastTradePrice = newPush.lastTradePrice ?: oldPush.lastTradePrice,
            changePrice = newPush.changePrice ?: oldPush.changePrice,
            percentageChange = newPush.percentageChange ?: oldPush.percentageChange,
        )
    }
}
