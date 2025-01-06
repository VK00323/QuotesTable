package com.example.data.websocket.dto

import com.google.gson.annotations.SerializedName

data class QuotesLabelRequestDto(

    @SerializedName("cmd")
    val cmd: String,

    @SerializedName("params")
    val params: QuoteLabelParamsDto,

    )
