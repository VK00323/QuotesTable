package com.example.core.network

sealed class ErrorType {
    data object Network : ErrorType()
    data class Server(val message: String?) : ErrorType()
    data class Unexpected(val e: Exception) : ErrorType()
}