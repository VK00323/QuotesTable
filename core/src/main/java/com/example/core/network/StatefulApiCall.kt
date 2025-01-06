package com.example.core.network

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

fun <T> statefulApiCall(
    apiCall: suspend () -> T,
) = flow {
    emit(LoadingState.Loading)

    try {
        val callResult = apiCall()
        emit(LoadingState.Data(callResult))
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        emit(LoadingState.Error(ErrorType.Server(errorBody)))
    } catch (e: IOException) {
        emit(LoadingState.Error(ErrorType.Network))
    } catch (e: Exception) {
        emit(LoadingState.Error(ErrorType.Unexpected(e)))
    }
}.distinctUntilChanged()
