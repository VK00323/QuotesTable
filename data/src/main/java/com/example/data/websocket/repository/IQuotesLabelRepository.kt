package com.example.data.websocket.repository

import com.example.core.network.LoadingState
import com.example.data.websocket.dto.QuotesLabelRequestDto
import kotlinx.coroutines.flow.Flow

interface IQuotesLabelRepository {

    fun getQuotesLabel(request: QuotesLabelRequestDto): Flow<LoadingState<List<String>>>
}