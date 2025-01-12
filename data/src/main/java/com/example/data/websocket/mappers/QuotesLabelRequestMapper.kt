package com.example.data.websocket.mappers

import com.example.data.websocket.dto.QuotesLabelRequestDto
import com.example.data.websocket.entities.QuotesLabelRequest

fun QuotesLabelRequest.mapToQuotesUpdatesDataDto() = QuotesLabelRequestDto(
    cmd = cmd,
    params = params.mapToQuotesUpdatesDataDto(),
)