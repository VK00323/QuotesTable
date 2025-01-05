package com.example.quotes

data class QuotesState(
    val quotes: List<Quote> = listOf(Quote()),
    val isLoading: Boolean = false,
)