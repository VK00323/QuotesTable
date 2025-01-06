package com.example.quotes.usecase

import com.example.core.network.LoadingState
import com.example.data.websocket.dto.QuoteLabelParamsDto
import com.example.data.websocket.dto.QuotesLabelRequestDto
import com.example.data.websocket.repository.IQuotesLabelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesLabelUseCase @Inject constructor(
    private val quotesRepository: IQuotesLabelRepository,
) {

    fun getQuotesLabel(): Flow<LoadingState<List<String>>> {
        return quotesRepository.getQuotesLabel(
            QuotesLabelRequestDto(
                "getTopSecurities",
                QuoteLabelParamsDto(
                    "stocks",
                    "russia",
                    0,
                    30,
                ),
            )
        )
    }
}