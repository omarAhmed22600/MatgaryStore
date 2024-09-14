package com.brandsin.store.network

sealed class ResponseHandler<out T> {
    object Loading : ResponseHandler<Nothing>()
    object StopLoading : ResponseHandler<Nothing>()
    data class Success<out T>(val data: T) : ResponseHandler<T>()
    data class Error(val message: String) : ResponseHandler<Nothing>()
    data class ErrorException(val message: Exception) : ResponseHandler<Nothing>()
}
