package com.example.quotes.model

data class QuotesState(
    val quotes: List<Quote> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    )