package com.example.data.websocket.mappers

import com.example.data.websocket.dto.QuotesLabelResponseDto
import com.example.data.websocket.entities.QuotesLabelResponse

fun QuotesLabelResponseDto.mapToQuotesUpdatesData() = QuotesLabelResponse(
    tickers = tickers,
)