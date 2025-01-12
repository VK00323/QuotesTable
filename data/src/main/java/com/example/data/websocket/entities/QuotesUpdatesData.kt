package com.example.data.websocket.entities

data class QuotesUpdatesData(
    val ticker: String,
    val exchangeLatestTrade: String?,
    val nameLatin: String?,
    val minStep: Double?,
    val lastTradePrice: Double?,
    val changePrice: Double?,
    val percentageChange: Double?,
)
