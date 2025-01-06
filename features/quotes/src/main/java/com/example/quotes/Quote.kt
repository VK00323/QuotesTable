package com.example.quotes

data class Quote(
    val ticker: String,
    val changePercent: Double? = null,
    val name: String? = null,
    val lastPrice: Double? = null,
    val priceChange: Double? = null,
    val exchangeLatestTrade: String? = null,
    val minStep : Double? = null,
)