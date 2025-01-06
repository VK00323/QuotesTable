package com.example.data.websocket.dto

import com.google.gson.annotations.SerializedName

data class QuoteLabelParamsDto(

    @SerializedName("type")
    val type: String,

    @SerializedName("exchange")
    val exchange: String,

    @SerializedName("gainers")
    val gainers: Int,

    @SerializedName("limit")
    val limit: Int,
)
