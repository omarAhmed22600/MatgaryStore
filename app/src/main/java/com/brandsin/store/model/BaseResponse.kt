package com.brandsin.store.model

data class BaseResponse<T>(
    val `data`: T,
    val success: Boolean
)