package com.example.data.websocket.api

import com.example.data.websocket.dto.QuotesLabelRequestDto
import com.example.data.websocket.dto.QuotesLabelResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface QuotesLabelApi {

    @POST("tradernet-api/quotes-get-top-securities")
    suspend fun getQuotesLabel(
        @Body request: QuotesLabelRequestDto,
    ): QuotesLabelResponseDto
}