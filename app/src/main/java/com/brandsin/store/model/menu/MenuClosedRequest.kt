package com.brandsin.store.model.menu

import com.google.gson.annotations.SerializedName

data class MenuClosedRequest(

        @SerializedName("is_closed")
        var isClosed: Int? = null,

        @SerializedName("store_id")
        var storeId: Int? = null
)