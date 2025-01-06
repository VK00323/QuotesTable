package com.example.quotes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.WebSocketEvent
import com.example.core.network.LoadingState
import com.example.quotes.usecase.GetQuotesLabelUseCase
import com.example.quotes.usecase.QuotesUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val labelUseCase: GetQuotesLabelUseCase,
    private val quotesUpdatesUseCase: QuotesUpdatesUseCase,
) : ViewModel() {

    private val _quotesTableState = MutableStateFlow(QuotesState())
    val quotesTableState: StateFlow<QuotesState> = _quotesTableState.asStateFlow()

    init {
        observeQuotesUpdateEvent()
        quotesUpdatesUseCase.connect()
        getQuotesLabel()
        // Todo Вынести Подключение к WebSocket
        // Todo нужно останавливать в бэкграунде и возобновлять при появлении на переднем плане
    }

    private fun getQuotesLabel() {
        viewModelScope.launch {
            labelUseCase.getQuotesLabel().collect { state ->
                when (state) {
                    is LoadingState.Data -> {
                        sendMessage(state.data)
                        println("Received QuotesUpdateEvent: $state")
                        val labels = state.data
                        val quotes = labels.map { Quote(ticker = it) }
                        _quotesTableState.update { value ->

                            value.copy(
                                quotes = quotes,
                                isLoading = false,
                            )

                        }
                    }

                    is LoadingState.Loading -> {
                        _quotesTableState.update { value ->
                            value.copy(isLoading = true)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun observeQuotesUpdateEvent() {
        viewModelScope.launch {
            quotesUpdatesUseCase.observeEvents()
                .collect { event ->
                    if (event is WebSocketEvent.QuotesUpdateEvent) {
                        // Обновляем только конкретный элемент по тикеру
                        _quotesTableState.update { currentState ->
                            val updatedQuotes = currentState.quotes.map { quote ->
                                if (quote.ticker == event.data.c) {
                                    quote.copy(
                                        exchangeLatestTrade = event.data.ltr
                                            ?: quote.exchangeLatestTrade,
                                        name = event.data.name2 ?: quote.name,
                                        lastPrice = event.data.ltp ?: quote.lastPrice,
                                        minStep = event.data.minStep ?: quote.minStep,
                                        priceChange = event.data.chg ?: quote.priceChange,
                                        changePercent = event.data.pcp ?: quote.changePercent,
                                    )
                                } else {
                                    quote
                                }
                            }

                            currentState.copy(
                                quotes = updatedQuotes
                            )
                        }
                    }
                }
        }
    }

    private fun sendMessage(tickers: List<String>) {
        val tickersArray = JSONArray(tickers)
        val subscriptionMessage = JSONArray().apply {
            put("realtimeQuotes")
            put(tickersArray)
        }
        quotesUpdatesUseCase.sendMessage(subscriptionMessage.toString())
        Log.i("WebSocket", "Subscription sent: $subscriptionMessage")
    }
}