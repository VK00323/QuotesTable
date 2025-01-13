package com.example.quotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.ErrorType
import com.example.core.network.LoadingState
import com.example.data.websocket.entities.QuotesUpdatesData
import com.example.quotes.enums.HighlightColorEnum
import com.example.quotes.model.Quote
import com.example.quotes.model.QuotesState
import com.example.quotes.usecase.GetQuotesLabelUseCase
import com.example.quotes.usecase.QuotesUpdatesUseCase
import com.example.quotes.usecase.SendWebSocketMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val labelUseCase: GetQuotesLabelUseCase,
    private val quotesUpdatesUseCase: QuotesUpdatesUseCase,
    private val sendMessageUseCase: SendWebSocketMessageUseCase,
) : ViewModel() {

    companion object {
        const val REALTIME_QUOTES = "realtimeQuotes"
    }

    private val _quotesTableState = MutableStateFlow<QuotesState>(QuotesState.Loading)
    val quotesTableState: StateFlow<QuotesState> = _quotesTableState.asStateFlow()

    private val highlightJobs = mutableMapOf<String, Job>()
    private var getQuotesLabelJob: Job? = null

    init {
        observeQuotesUpdateEvent()
        getQuotesLabel()
    }

    private fun getQuotesLabel() {
        getQuotesLabelJob?.cancel()
        getQuotesLabelJob = viewModelScope.launch {
            labelUseCase().collect { state ->
                when (state) {
                    is LoadingState.Data -> {
                        sendMessage(state.data)
                        val labels = state.data
                        val quotes = labels.map { Quote(ticker = it) }
                        _quotesTableState.value = QuotesState.ShowQuotes(quotes)
                    }

                    is LoadingState.Loading, LoadingState.Idle  -> {
                        _quotesTableState.value = QuotesState.Loading
                    }

                    is LoadingState.Error -> handleNetworkError(state.errorType)
                }
            }
        }
    }

    private fun handleNetworkError(errorType: ErrorType) {
        if (errorType is ErrorType.Network) {
            _quotesTableState.value = QuotesState.Error
        }
    }

    private fun observeQuotesUpdateEvent() {
        viewModelScope.launch {
            quotesUpdatesUseCase.observeQuotesUpdateEvents()
                .collect { events ->
                    if (events.isNotEmpty()) handleQuotesUpdate(events)
                }
        }
    }

    private fun handleQuotesUpdate(events: List<QuotesUpdatesData>) {
        val updatesByTicker = events.associateBy { it.ticker }
        val currentState = _quotesTableState.value
        if (currentState is QuotesState.ShowQuotes) {
            val updatedQuotes = currentState.quotes.map { quote ->
                updatesByTicker[quote.ticker]?.let { update ->
                    quote.updateWith(update)
                } ?: quote
            }
            _quotesTableState.value = QuotesState.ShowQuotes(updatedQuotes)
        }
    }

    private fun Quote.updateWith(data: QuotesUpdatesData): Quote = copy(
        exchangeLatestTrade = data.exchangeLatestTrade ?: exchangeLatestTrade,
        name = data.nameLatin ?: name,
        minStep = data.minStep ?: minStep,
        lastPrice = roundToMinStep(
            data.lastTradePrice,
            data.minStep ?: minStep,
        ) ?: lastPrice,
        changePrice = roundToMinStep(
            data.changePrice,
            data.minStep ?: minStep,
        ) ?: changePrice,
        isHighlightNeeded = processHighlight(
            ticker = ticker,
            old = percentageChange,
            new = data.percentageChange,
            isInitialLoad = isInitialLoad,
        ),
        highlightColor = getHighlightColor(
            percentageChange,
            data.percentageChange,
        ),
        percentageChange = data.percentageChange ?: percentageChange,
        isInitialLoad = false,
    )

    private fun getHighlightColor(old: Double, new: Double?): HighlightColorEnum =
        when {
            new == null || old == new -> HighlightColorEnum.TRANSPARENT
            old > new -> HighlightColorEnum.RED
            else -> HighlightColorEnum.GREEN
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

    private fun processHighlight(
        ticker: String,
        old: Double,
        new: Double?,
        isInitialLoad: Boolean,
    ): Boolean {
        val isHighlightNeeded = shouldHighlight(old, new, isInitialLoad)
        if (isHighlightNeeded) {
            scheduleHighlightReset(ticker)
        }
        return isHighlightNeeded
    }

    private fun shouldHighlight(old: Double, new: Double?, isInitialLoad: Boolean) =
        !(isInitialLoad || new == null || old == new)

    private fun scheduleHighlightReset(ticker: String) {
        highlightJobs[ticker]?.cancel()

        val job = viewModelScope.launch {
            delay(500)
            val currentState = _quotesTableState.value
            if (currentState is QuotesState.ShowQuotes) {
                val resetQuotes = currentState.quotes.map { quote ->
                    if (quote.ticker == ticker) {
                        quote.copy(
                            isHighlightNeeded = false,
                            highlightColor = HighlightColorEnum.TRANSPARENT,
                        )
                    } else {
                        quote
                    }
                }
                _quotesTableState.value = QuotesState.ShowQuotes(resetQuotes)
            }
        }
        highlightJobs[ticker] = job
    }

    private fun sendMessage(tickers: List<String>) {
        val tickersArray = JSONArray(tickers)
        val subscriptionMessage = JSONArray().apply {
            put(REALTIME_QUOTES)
            put(tickersArray)
        }
        sendMessageUseCase(subscriptionMessage.toString())
    }

    fun onRetryClick() {
        getQuotesLabel()
    }
}