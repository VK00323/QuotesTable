package com.example.quotes.model

import android.graphics.Color

data class Quote(
    val ticker: String,
    val percentageChange: Double = 0.0,
    val name: String? = null,
    val lastPrice: Double? = null,
    val changePrice: Double = 0.0,
    val exchangeLatestTrade: String? = null,
    val minStep: Double = 0.001,
    val isHighlightNeeded: Boolean = false,
    val highlightColor: Int = Color.TRANSPARENT,
    val isInitialLoad: Boolean = true
)