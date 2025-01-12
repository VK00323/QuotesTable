package com.example.quotes.usecase

import com.example.core.network.LoadingState
import com.example.data.websocket.entities.QuoteLabelParams
import com.example.data.websocket.entities.QuotesLabelRequest
import com.example.data.websocket.repository.IQuotesLabelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesLabelUseCase @Inject constructor(
    private val quotesRepository: IQuotesLabelRepository,
) {

    operator fun invoke(): Flow<LoadingState<List<String>>> {
        return quotesRepository.getQuotesLabel(
            QuotesLabelRequest(
                "getTopSecurities",
                QuoteLabelParams(
                    "stocks",
                    "russia",
                    0,
                    30,
                ),
            )
        )
    }
}