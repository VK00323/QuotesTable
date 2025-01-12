package com.example.data.websocket.repository

import com.example.core.network.LoadingState
import com.example.data.websocket.entities.QuotesLabelRequest
import kotlinx.coroutines.flow.Flow

interface IQuotesLabelRepository {

    fun getQuotesLabel(request: QuotesLabelRequest): Flow<LoadingState<List<String>>>
}