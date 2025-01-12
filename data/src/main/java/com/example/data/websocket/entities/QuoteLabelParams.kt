package com.example.data.websocket.entities

data class QuoteLabelParams(
    val type: String,
    val exchange: String,
    val gainers: Int,
    val limit: Int,
)
