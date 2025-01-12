package com.example.data.websocket.mappers

import com.example.data.websocket.dto.QuoteLabelParamsDto
import com.example.data.websocket.entities.QuoteLabelParams

fun QuoteLabelParams.mapToQuotesUpdatesDataDto() =  QuoteLabelParamsDto(
    type = type,
    exchange = exchange,
    gainers = gainers,
    limit = limit,
)