package com.example.data.websocket.repository

import com.example.core.network.ErrorType
import com.example.core.network.LoadingState
import com.example.data.websocket.dataSource.IQuotesLabelApiDataSource
import com.example.data.websocket.dataSource.IQuotesLabelLocalDataSource
import com.example.data.websocket.entities.QuotesLabelRequest
import com.example.data.websocket.mappers.mapToQuotesUpdatesDataDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QuotesLabelRepository @Inject constructor(
    private val api: IQuotesLabelApiDataSource,
    private val local: IQuotesLabelLocalDataSource,
) : IQuotesLabelRepository {

    override fun getQuotesLabel(request: QuotesLabelRequest): Flow<LoadingState<List<String>>> =
        flow {
            emit(LoadingState.Loading)

            api.getQuotesLabel(request.mapToQuotesUpdatesDataDto()).collect { apiResult ->
                when (apiResult) {
                    is LoadingState.Loading -> emit(LoadingState.Loading)
                    is LoadingState.Data -> {
                        val result = apiResult.data.tickers
                        emit(LoadingState.Data(result))
                    }
                    //Todo это костыль, потому что сервер не работает
                    is LoadingState.Error -> {
                        if (apiResult.errorType == ErrorType.Network) {
                            emit(LoadingState.Error(apiResult.errorType))
                        } else {
                            emit(LoadingState.Data(local.getQuotesLabel()))
                        }
                    }

                    LoadingState.Idle -> emit(LoadingState.Idle)
                }
            }
        }
}
