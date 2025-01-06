package com.example.data.websocket.dto

import com.google.gson.annotations.SerializedName

data class QuotesLabelResponseDto(

    @SerializedName("tickers")
    val tickers: List<String>,
)