package com.example.data.websocket.entities

data class QuotesLabelRequest(
    val cmd: String,
    val params: QuoteLabelParams,
)