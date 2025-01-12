package com.example.quotes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.ErrorType
import com.example.core.network.LoadingState
import com.example.data.websocket.entities.QuotesUpdatesData
import com.example.data.websocket.events.WebSocketEvent
import com.example.quotes.model.Quote
import com.example.quotes.model.QuotesState
import com.example.quotes.usecase.GetQuotesLabelUseCase
import com.example.quotes.usecase.QuotesUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val labelUseCase: GetQuotesLabelUseCase,
    private val quotesUpdatesUseCase: QuotesUpdatesUseCase,
) : ViewModel() {

    companion object {
        const val REALTIME_QUOTES = "realtimeQuotes"
    }

    private val _quotesTableState = MutableStateFlow(QuotesState())
    val quotesTableState: StateFlow<QuotesState> = _quotesTableState.asStateFlow()

    init {
        observeQuotesUpdateEvent()
        getQuotesLabel()
    }

    private fun getQuotesLabel() {
        viewModelScope.launch {
            labelUseCase().collect { state ->
                when (state) {
                    is LoadingState.Data -> {
                        sendMessage(state.data)
                        val labels = state.data
                        val quotes = labels.map { Quote(ticker = it) }
                        _quotesTableState.update { value ->
                            value.copy(
                                quotes = quotes,
                                isError = false,
                            )
                        }
                    }

                    is LoadingState.Loading -> {
                        _quotesTableState.update { value ->
                            value.copy(isLoading = true)
                        }
                    }

                    is LoadingState.Error -> handleNetworkError(state.errorType)

                    else -> {}
                }
            }
        }
    }

    private fun handleNetworkError(errorType: ErrorType) {
        if (errorType is ErrorType.Network) {
            _quotesTableState.update { value ->
                value.copy(
                    isLoading = false,
                    isError = true,
                )
            }
        }
    }

    private fun observeQuotesUpdateEvent() {
        viewModelScope.launch {
            quotesUpdatesUseCase.observeEvents()
                .collect { event ->
                    if (event is WebSocketEvent.QuotesUpdateEvent) {
                        Log.d("Received QuotesUpdateEvent", "event: $event")
                        handleQuotesUpdate(event.data)
                    }
                }
        }
    }

    private fun handleQuotesUpdate(data: QuotesUpdatesData) {
        _quotesTableState.update { currentState ->
            currentState.copy(
                quotes = currentState.quotes.map { it.updateWith(data) },
                isLoading = false
            )
        }
    }

    private fun Quote.updateWith(data: QuotesUpdatesData): Quote {
        if (ticker != data.ticker) return this
        return copy(
            exchangeLatestTrade = data.exchangeLatestTrade ?: exchangeLatestTrade,
            name = data.nameLatin ?: name,
            minStep = data.minStep ?: minStep,
            lastPrice = roundToMinStep(data.lastTradePrice, data.minStep ?: minStep) ?: lastPrice,
            changePrice = roundToMinStep(data.changePrice, data.minStep ?: minStep)
                ?: changePrice,
            percentageChange = data.percentageChange ?: percentageChange,
        )
    }

    private fun roundToMinStep(value: Double?, minStep: Double): Double? {
        if (value == null) return null
        val bigDecimalValue = BigDecimal.valueOf(value)
        val bigDecimalMinStep = BigDecimal.valueOf(minStep)
        val roundedValue = bigDecimalValue.divide(bigDecimalMinStep, 0, RoundingMode.DOWN)
            .multiply(bigDecimalMinStep)
        val plainString = roundedValue.stripTrailingZeros().toPlainString()
        return BigDecimal(plainString).toDouble()
    }

    private fun sendMessage(tickers: List<String>) {
        val tickersArray = JSONArray(tickers)
        val subscriptionMessage = JSONArray().apply {
            put(REALTIME_QUOTES)
            put(tickersArray)
        }
        quotesUpdatesUseCase.sendMessage(subscriptionMessage.toString())
    }

    fun onRetryClick() {
        getQuotesLabel()
    }
}