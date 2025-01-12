package com.example.quotes.usecase

import com.example.data.websocket.entities.QuotesUpdatesData
import com.example.data.websocket.events.WebSocketEvent
import com.example.data.websocket.repository.QuotesUpdatesRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

class QuotesUpdatesUseCase @Inject constructor(
    private val repository: QuotesUpdatesRepository,
) {
    private companion object {
        private const val QUOTES_UPDATES_ACCUMULATION_DELAY = 1000L
    }

    fun sendMessage(message: String) = repository.sendMessage(message)

    @OptIn(FlowPreview::class)
    fun observeQuotesUpdateEvents(): Flow<List<QuotesUpdatesData>> {
        return repository.observeEvents()
            .filterIsInstance<WebSocketEvent.QuotesUpdateEvent>()
            .scan(mutableMapOf<String, QuotesUpdatesData>()) { eventsBuffer, push ->
                val eventData = push.data
                val existingPush = eventsBuffer[eventData.ticker]
                eventsBuffer[eventData.ticker] = if (existingPush != null) {
                    mergePushes(existingPush, eventData)
                } else {
                    eventData
                }
                eventsBuffer
            }
            .debounce(QUOTES_UPDATES_ACCUMULATION_DELAY)
            .map { currentMap ->
                val updatedList = currentMap.values.toList()
                currentMap.clear()
                updatedList
            }
    }

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
