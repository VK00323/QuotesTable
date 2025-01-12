package com.example.quotes.model

data class Quote(
    val ticker: String,
    val percentageChange: Double = 0.0,
    val name: String? = null,
    val lastPrice: Double? = null,
    val changePrice: Double = 0.0,
    val exchangeLatestTrade: String? = null,
    val minStep: Double = 0.001,
)