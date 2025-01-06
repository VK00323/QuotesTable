package com.example.core.network

sealed interface LoadingState<out T> {
    data object Idle : LoadingState<Nothing>
    data object Loading : LoadingState<Nothing>
    data class Error(val errorType: ErrorType) : LoadingState<Nothing>
    data class Data<T>(val data: T) : LoadingState<T>
}