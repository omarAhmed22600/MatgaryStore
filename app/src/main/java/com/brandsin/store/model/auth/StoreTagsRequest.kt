package com.brandsin.store.model.auth

import com.google.gson.annotations.SerializedName

data class StoreTagsRequest (
        @SerializedName("categories")
        var category: ArrayList<Int>? = null,

        @SerializedName("locale")
        var locale: String? = null
)