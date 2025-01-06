package com.example.data.websocket.dataSource

import com.example.core.network.statefulApiCall
import com.example.data.websocket.api.QuotesLabelApi
import com.example.data.websocket.dto.QuotesLabelRequestDto
import javax.inject.Inject

class QuotesLabelApiDataSource  @Inject constructor(
    private val api: QuotesLabelApi,
) : IQuotesLabelApiDataSource {
    override suspend fun getQuotesLabel(request: QuotesLabelRequestDto) =
        statefulApiCall {
            return@statefulApiCall api.getQuotesLabel(request)
        }
}