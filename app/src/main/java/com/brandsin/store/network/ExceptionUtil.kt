package com.brandsin.store.network

import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException

object ExceptionUtil {

    fun Exception.getExceptionMessage(): String {
        Timber.e(this)
        return when (this) {
            is SocketTimeoutException -> "timeout, please try again"
            is HttpException -> "${this.javaClass.name}: HTTP 405 Method Not Allowed, check your network or open VPN"
            is NullPointerException -> this.message ?: "null value found"
            is IndexOutOfBoundsException -> "something wrong is happened, please try again"
            else -> this.message ?: "null exc found"
        }
    }
}