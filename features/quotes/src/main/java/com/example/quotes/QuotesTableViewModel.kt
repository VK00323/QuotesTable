package com.example.quotes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
//    private val labelUseCase: GetQuotesLabelUseCase,
//    private val quotesUpdatesUseCase: QuotesUpdatesUseCase,
) : ViewModel() {

    private val _quotesTableState = MutableStateFlow(QuotesState())
    val quotesTableState: StateFlow<QuotesState> = _quotesTableState.asStateFlow()


    init {
        // Todo Вынести Подключение к WebSocket
//        quotesUpdatesUseCase.connect()
        observeQuotesUpdateEvent()
    }

    private fun observeQuotesUpdateEvent() {
//        viewModelScope.launch {
//            quotesUpdatesUseCase.observeEvents()
//                .collect { event ->
//                    if (event is WebSocketEvent.QuotesUpdateEvent) {
//                        println("Received QuotesUpdateEvent: $event")
//                    }
//                }
//        }
    }

    //todo Этого тут быть не должно. Только для проверки
    fun sendMessage() {
        val tickersToWatchChanges = listOf(
            "SP500.IDX", "AAPL.US", "RSTI", "GAZP", "MRKZ", "RUAL", "HYDR", "MRKS", "SBER",
            "FEES", "TGKA", "VTBR", "ANH.US", "VICL.US", "BURG.US", "NBL.US", "YETI.US", "WSFS.US",
            "NIO.US", "DXC.US", "MIC.US", "HSBC.US", "EXPN.EU", "GSK.EU", "SHP.EU", "MAN.EU",
            "DB1.EU", "MUV2.EU", "TATE.EU", "KGF.EU", "MGGT.EU", "SGGD.EU"
        )
        val tickersArray = JSONArray(tickersToWatchChanges)
        val subscriptionMessage = JSONArray().apply {
            put("quotes")
            put(tickersArray)
        }
//        quotesUpdatesUseCase.sendMessage(subscriptionMessage.toString())
        Log.i("WebSocket", "Subscription sent: $subscriptionMessage")
    }

    //Todo удалить, только для теста
//    private val _quotes = MutableStateFlow<List<QuotesLabelResponseDto>>(emptyList())
//    val quotes: StateFlow<List<QuotesLabelResponseDto>> get() = _quotes
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> get() = _error
//    private val _loading = MutableStateFlow(false)
//    val loading: StateFlow<Boolean> get() = _loading

    //Todo поправить, только для теста
//    fun getQuotesLabel() {
//        viewModelScope.launch {
//            _loading.value = true
//            labelUseCase.invoke(
//                QuotesLabelRequestDto(
//                    cmd = "getTopSecurities",
//                    params = listOf(
//                        QuoteLabelParamsDto(
//                            type = "stocks",
//                            exchange = "russia",
//                            gainers = 0,
//                            limit = 30,
//                        )
//                    )
//                )
//            )
//                .catch { e ->
//                    _error.value = e.message
//                    _loading.value = false
//                    Log.e("getQuotesLabel", "Error parsing message: ${e.message}")
//                    //Todo Тут можно просто список отдать тогда если запрос не работает
//                    val tickersToWatchChanges = listOf("SP500.IDX", "AAPL.US")
//                    _quotes.value = listOf(QuotesLabelResponseDto(tickersToWatchChanges))
//
//                }
//                .collect { quotes ->
//                    _quotes.value = quotes
//                    _loading.value = false
//                    Log.d("getQuotesLabel", "quotes: ${quotes}")
//                }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
//        repository.disconnectWebSocket()
    }

}




