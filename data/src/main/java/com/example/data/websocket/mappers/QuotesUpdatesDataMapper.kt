package com.example.data.websocket.mappers

import com.example.data.websocket.dto.events.QuotesUpdatesDataDto
import com.example.data.websocket.entities.QuotesUpdatesData

fun QuotesUpdatesDataDto.mapToQuotesUpdatesData() = QuotesUpdatesData(
    ticker = c,
    exchangeLatestTrade = ltr,
    nameLatin = name2,
    minStep = minStep,
    lastTradePrice = ltp,
    changePrice = chg,
    percentageChange = pcp,
)