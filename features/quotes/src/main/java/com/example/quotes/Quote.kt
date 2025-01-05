package com.example.quotes

data class Quote(
    val ticker: String? = null,
    val changePercent: Double? = null,
    val exchange: String? = null,
    val name: String? = null,
    val lastPrice: Double? = null,
    val priceChange: Double? = null,
)