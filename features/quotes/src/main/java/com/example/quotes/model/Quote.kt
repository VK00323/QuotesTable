package com.example.quotes.model

import com.example.quotes.enums.HighlightColorEnum

data class Quote(
    val ticker: String,
    val percentageChange: Double = 0.0,
    val name: String? = null,
    val lastPrice: Double? = 0.0,
    val changePrice: Double = 0.0,
    val exchangeLatestTrade: String? = "",
    val minStep: Double = 0.001,
    val isHighlightNeeded: Boolean = false,
    val highlightColor: HighlightColorEnum = HighlightColorEnum.TRANSPARENT,
    val isInitialLoad: Boolean = true
)