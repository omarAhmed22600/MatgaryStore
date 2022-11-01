package com.brandsin.store.model.menu

import com.google.gson.annotations.SerializedName

data class MenuBusyRequest (

        @SerializedName("is_busy")
        var isBusy: Int? = null,

        @SerializedName("store_id")
        var storeId: Int? = null
)