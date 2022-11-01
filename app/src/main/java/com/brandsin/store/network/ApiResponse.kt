package com.brandsin.store.network

/*
 * Created by MouazSalah on 29/12/2020.
 */
data class ApiResponse<out T>(val status: Status, val data: T? = null, val message: String? = null) {

    companion object {
        fun <T> success(data: T?, msg: String? = null): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, msg)
        }
        fun <T> successMessage(msg: String?, data: T? = null): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS_MESSAGE, data, msg)
        }
        fun <T> errorMessage(msg: String?, data: T? = null): ApiResponse<T> {
            return ApiResponse(Status.ERROR_MESSAGE, data, msg)
        }
        fun <T> loading(data: T?): ApiResponse<T> {
            return ApiResponse(Status.LOADING, data)
        }
    }
}