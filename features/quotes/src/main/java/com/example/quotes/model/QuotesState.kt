package com.example.quotes.model

sealed interface QuotesState {
    data class ShowQuotes(val quotes: List<Quote> = listOf()) : QuotesState
    data object Loading : QuotesState
    data object Error : QuotesState
}
