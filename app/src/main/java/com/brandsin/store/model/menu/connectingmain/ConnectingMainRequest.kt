package com.brandsin.store.model.menu.connectingmain

import com.google.gson.annotations.SerializedName

data class ConnectingMainRequest (

    @SerializedName("from_store_id")
    var fromStoreId: String? = null,

    @SerializedName("store_id")
    var storeId: Int? = null
)