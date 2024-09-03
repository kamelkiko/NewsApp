package com.kamel.newsapp.presentation.base

sealed interface ErrorState {
    data class NetworkError(val message: String?) : ErrorState
    data class NotFound(val message: String?) : ErrorState
    data class UnknownError(val message: String?) : ErrorState
    data class ServerError(val message: String?) : ErrorState
}