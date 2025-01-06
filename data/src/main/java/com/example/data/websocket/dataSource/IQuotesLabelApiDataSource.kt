package com.example.data.websocket.dataSource

import com.example.core.network.LoadingState
import com.example.data.websocket.dto.QuotesLabelRequestDto
import com.example.data.websocket.dto.QuotesLabelResponseDto
import kotlinx.coroutines.flow.Flow

interface IQuotesLabelApiDataSource {

    suspend fun getQuotesLabel(request: QuotesLabelRequestDto): Flow<LoadingState<QuotesLabelResponseDto>>

}